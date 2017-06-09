package vertx

/**
 * Created by jackqack on 06/06/17.
 */


import cmrdt.set.operation.ORSetOperation
import io.vertx.core.AbstractVerticle
import io.vertx.core.Context
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.net.*
import node.CrdtNode
import node.Node
import java.io.File


class VerticleNode : AbstractVerticle() {

    companion object {
        const val DEFAULT_NODES_PATH = "nodes.txt"
        const val PORT = 10390

        const val CLIENT_CONNECT_TIMEOUT = 7500
        const val CLIENT_RECONNECT_INTERVAL = 5000L
        const val CLIENT_RECONNECT_ATTEMPTS = Int.MAX_VALUE
    }


    val node = Node("1", "127.0.0.1", PORT)
    var server: NetServer? = null
    val clients: MutableMap<Node, NetClient> = hashMapOf()
    val crdt = object : CrdtNode<Int>(node) {

        override fun sendOperation(node: Node, operation: ORSetOperation<Int>) {
            // TODO: send to every opened socket
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun requestInitialState(node: Node) {
            // TODO:
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }


    override fun init(vertx: Vertx?, context: Context?) {
        super.init(vertx, context)
        if (vertx != null) {
            initServer(vertx)
            for (to in readNodesFromFile(DEFAULT_NODES_PATH)) {
                addClient(vertx, to)
            }
        }
    }

    private fun initServer(vertx: Vertx) {
        val options = NetServerOptions()
        server = vertx.createNetServer(options)
        server?.connectHandler(this::onNewNodeConnected)
    }

    override fun start(startFuture: Future<Void>?) {

        // connect server
        server?.listen(PORT) { result ->
            if (result.succeeded()) {
                // TODO:
                crdt.start(clients.keys)
                println("server started")
                startFuture?.complete()
            } else {
                // TODO:
                println("server failed: ${result.cause()}")
                startFuture?.fail(result.cause())
            }
        }

        // connect all clients
        for ((to, client) in clients) {
            client.connect(to.port, to.address, { res ->
                if (res.succeeded()) {
                    val socket = res.result()
                    // TODO:
                } else {
                    // TODO:
                }
            })
        }
    }

    override fun stop(stopFuture: Future<Void>?) {
        super.stop(stopFuture)
        for ((_, client) in clients) client.close()
        server?.close({ result ->
            if (result.succeeded()) stopFuture?.complete()
            else stopFuture?.fail(result.cause())
        })
    }

    // This handler is called whenever a new TCP connection is created by another node
    private fun onNewNodeConnected(socket: NetSocket) {
        // TODO:
        println("New incoming TCP connection!")
        socket.handler({
            println("incoming data (${it.length()} bytes): ${it.getString(0, it.length())}")
        })
        socket.handler { println("second handler") }
    }

    private fun addClient(vertx: Vertx, to: Node) {
        val options = NetClientOptions()
                .setConnectTimeout(CLIENT_CONNECT_TIMEOUT)
                .setReconnectInterval(CLIENT_RECONNECT_INTERVAL)
                .setReconnectAttempts(CLIENT_RECONNECT_ATTEMPTS)
        val client = vertx.createNetClient(options)

        clients.put(to, client)
    }

}


private fun readNodesFromFile(path: String): List<Node> {
    val list = ArrayList<Node>()
    val file = File(path)
    try {
        if (file.isFile && file.canRead()) {
            file.readLines().forEach {
                val ss = it.trim().split(" ")
                if (ss.size == 3) {
                    val id = ss[0]
                    val ip = ss[1]
                    val port = ss[2].toIntOrNull()
                    if (port != null) {
                        list.add(Node(id, ip, port))
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