package crdt.counter

import crdt.CRDT

/**
 * Created by jackqack on 21/05/17.
 */


/**
 * Increment-decrement counter.
 */
internal class PNCounter : CRDT<PNCounter> {

    private val increments: GCounter
    private val decrements: GCounter


    constructor() {
        increments = GCounter()
        decrements = GCounter()
    }

    private constructor(incs: GCounter, decs: GCounter) {
        increments = incs.copy()
        decrements = decs.copy()
    }

    /**
     * Increment counter by the given key.
     */
    fun increment(key: String) {
        increments.increment(key)
    }

    /**
     * Returns the immutable value of this CRDT.
     */
    fun value(): Int {
        return increments.value() - decrements.value()
    }

    /**
     * Decrement counter by the given key.
     */
    fun decrement(key: String) {
        decrements.increment(key)
    }

    override fun merge(other: PNCounter) {
        increments.merge(other.increments)
        decrements.merge(other.decrements)
    }

    override fun copy(): PNCounter {
        return PNCounter(this.increments.copy(), this.decrements.copy())
    }
}
