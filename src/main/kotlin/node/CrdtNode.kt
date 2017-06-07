package node

import cmrdt.set.ORSet
import cmrdt.set.operation.ORSetOperation
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * Created by jackqack on 06/06/17.
 */
abstract class CrdtNode<T>(val node: Node) {

    data class Node(val id: String, val address: String, val port: Int)

    private val downstreamOps: MutableMap<Node, Queue<ORSetOperation<T>>> = HashMap()
    private val nodes: MutableSet<Node> = HashSet()
    private val isSending: MutableSet<Node> = HashSet()

    private var set: ORSet<T>? = null
    private var isRunning = false
    private var isInitialized = false


    // CRDT node methods


    /**
     * Start the node.
     * This node creates completely new CRDT if given list of
     * nodes is null or empty. Otherwise this node sends
     * a request to other nodes for an initial state.
     */
    fun start(initialNodes: Collection<Node>?) {
        if (isRunning) return

        isRunning = true
        clear()
        if (initialNodes == null || initialNodes.isEmpty()) {
            // start new CRDT instance immediately
            isInitialized = true
            set = ORSet(this::onDownstream)
        } else {
            // send request for an the initial CRDT state and wait for response from anyone
            nodes.addAll(initialNodes)
            for (node in nodes) {
                requestInitialState(node)
            }
        }
    }

    /**
     * Stop the node and clear all inner data.
     */
    fun stop() {
        if (!isRunning) return

        isRunning = false
        isInitialized = false
        clear()
    }

    /**
     * Method must be called whenever new node is
     * connecting to the existing distributed CRDT.
     */
    fun addNode(node: Node) {
        if (!isInitialized) return
        if (downstreamOps.containsKey(node))
            downstreamOps[node]!!.clear()
        else
            downstreamOps[node] = ArrayDeque()
    }

    /**
     * Return a copy of an inner ORSet structure.
     */
    fun getState(): Map<T, Set<String>>? = set?.getState()

    private fun clear() {
        isSending.clear()
        nodes.clear()
        for ((_, queue) in downstreamOps) queue.clear()
        downstreamOps.clear()
        set?.clear()
    }


    // CRDT peer-to-peer protocol


    /**
     * Method must be called when an initial CRDT state received
     * in response to request for an initial state.
     */
    fun setInitialState(state: MutableMap<T, MutableSet<String>>) {
        if (!isInitialized) {
            isInitialized = true
            set?.clear()
            set = ORSet(state, this::onDownstream)
        }
    }

    /**
     * Method must be called whenever new operation received
     * from any node.
     */
    fun applyOperation(op: ORSetOperation<T>) {
        if (isInitialized) set?.upgrade(op)
    }

    /**
     * Sends an operation to the given node.
     */
    abstract fun sendOperation(node: Node, operation: ORSetOperation<T>)

    /**
     * Requests given node for the initial CRDT state.
     */
    abstract fun requestInitialState(node: Node)

    /**
     * Method must be called whenever operation sent
     * by sendOperation is successfully finished.
     */
    fun onNextOperationSent(node: Node) {
        downstreamOps[node]?.poll()
        trySendNextOperation(node)
    }

    private fun trySendNextOperation(node: Node) {
        if (downstreamOps[node]?.isNotEmpty() ?: false) {
            sendOperation(node, downstreamOps[node]!!.peek())
        }
    }

    private fun onDownstream(op: ORSetOperation<T>) {
        for ((node, queue) in downstreamOps) {
            queue.add(op)
            trySendNextOperation(node)
        }
    }


    // Local set methods


    fun add(x: T) {
        set?.add(x)
    }

    fun addAll(elements: Collection<T>) {
        set?.addAll(elements)
    }

    fun contains(x: T): Boolean {
        return set?.contains(x) ?: false
    }

    fun remove(x: T) {
        set?.remove(x)
    }


}