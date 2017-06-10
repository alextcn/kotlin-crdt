package vertx

/**
 * Created by jackqack on 06/06/17.
 */


import cmrdt.set.operation.ORSetOperation
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.json.JsonObject
import io.vertx.core.net.*
import node.CrdtNode
import node.Node
import java.io.File
import java.util.*
import kotlin.collections.HashSet


class VerticleNode : AbstractVerticle() {

    companion object {
        const val ALLOW_UNKNOWN_NODES = false
        const val DEFAULT_NODES_PATH = "nodes.txt"
        const val PORT = 10352

        const val CLIENT_CONNECT_TIMEOUT = 7500
        const val CLIENT_RECONNECT_INTERVAL = 5000L
        const val CLIENT_RECONNECT_ATTEMPTS = Int.MAX_VALUE
    }


    val myNode = Node("127.0.0.1", PORT)
    var server: NetServer? = null
    val nodes: MutableMap<Node, Pair<NetClient, NetSocket?>> = hashMapOf()
    val crdt: CrdtNode<Int> = object : CrdtNode<Int>(myNode) {

        override fun sendOperation(node: Node, operation: ORSetOperation<Int>) {
            nodes[node]?.second?.write(toJson(operation).encode())
            onOperationSent(node)
        }

        override fun requestInitialState(node: Node) {
            nodes[node]?.second?.write(VerticleMessage(MessageType.REQUEST_STATE).toJson().encode())
        }

    }


    override fun init(vertx: Vertx?, context: Context?) {
        super.init(vertx, context)

        for (arg in context!!.processArgs()) {
            println(arg)
        }

        initServer(vertx!!)
        for (to in readNodesFromFile(DEFAULT_NODES_PATH)) {
            if (to != myNode) addClient(vertx, to)
        }
    }

    private fun initServer(vertx: Vertx) {
        val options = NetServerOptions()
        server = vertx.createNetServer(options)
        server?.connectHandler(this::onNewNodeConnected)
    }

    private fun addClient(vertx: Vertx, to: Node) {
        val options = NetClientOptions()
                .setConnectTimeout(CLIENT_CONNECT_TIMEOUT)
                .setReconnectInterval(CLIENT_RECONNECT_INTERVAL)
                .setReconnectAttempts(CLIENT_RECONNECT_ATTEMPTS)
        val client = vertx.createNetClient(options)

        nodes.put(to, Pair(client, null))
    }

    override fun start(startFuture: Future<Void>?) {

        // connect server
        server?.listen(PORT) { result ->
            if (result.succeeded()) {
                log("server started: $myNode")
                crdt.start(nodes.keys)
                startFuture?.complete()
            } else {
                startFuture?.fail(result.cause())
            }
        }

        // connect all nodes
        for ((to, client) in nodes) {
            client.first.connect(to.port, to.address, { res ->
                if (res.succeeded()) {
                    log("connected to: $to")
                    val socket = res.result()
                    nodes[to] = Pair(client.first, socket)
                    socket.handler({ buffer -> onMessageReceived(socket, buffer) })
                } else {
                    log("connection failed to: $to")
                }
            })
        }
    }

    override fun stop(stopFuture: Future<Void>?) {
        for ((_, client) in nodes) client.first.close()

        server?.close({ result ->
            if (result.succeeded()) stopFuture?.complete()
            else stopFuture?.fail(result.cause())
        })
    }

    // This handler is called whenever a new TCP connection is created by another myNode
    private fun onNewNodeConnected(serverSocket: NetSocket) {
        val node = Node(serverSocket.remoteAddress().host(), serverSocket.remoteAddress().port())
        if (ALLOW_UNKNOWN_NODES || nodes.containsKey(node)) {
            serverSocket.handler({ buffer -> onMessageReceived(serverSocket, buffer) })
        } else {
            serverSocket.close()
        }
    }

    private fun onMessageReceived(socket: NetSocket, buffer: Buffer) {
        val str = buffer.getString(0, buffer.length())
        val msg = parseMessage(str)
        if (msg != null) {
            when (msg.type) {
                MessageType.REQUEST_STATE -> {
                    if (crdt.getState() != null) {
                        // TODO: test CRDT state mapping
                        socket.write(VerticleMessage(MessageType.RESPONSE_STATE, JsonObject.mapFrom(crdt.getState()).encode())
                                .toJson().encode())
                    }
                }
                MessageType.RESPONSE_STATE -> {
                    if (!crdt.isInitialized() && msg.data != null) {
                        val state = parseState(msg.data)
                        if (state != null) crdt.setInitialState(state)
                    }
                }
                MessageType.SEND_OPERATION -> {
                    if (crdt.isInitialized() && msg.data != null) {
                        val op = parseOperation(msg.data)
                        if (op != null) crdt.applyOperation(op)
                    }
                }
            }
        }
    }

    private fun onOperationSent(node: Node) {
        crdt.onNextOperationSent(node)
    }

}


private fun readNodesFromFile(path: String): MutableSet<Node> {
    val list = HashSet<Node>()
    val file = File(path)
    try {
        if (file.isFile && file.canRead()) {
            file.readLines().forEach {
                val ss = it.trim().split(" ")
                if (ss.size == 2) {
                    val ip = ss[0]
                    val port = ss[1].toIntOrNull()
                    if (port != null) {
                        list.add(Node(ip, port))
                    }
                }
            }
        }
    } catch (e: Exception) {
        println(e.localizedMessage)
        e.printStackTrace()
    }
    return list
}

private fun log(message: String) {
    println(message)
}