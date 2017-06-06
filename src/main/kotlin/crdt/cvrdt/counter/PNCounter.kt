package cvrdt.counter

import cvrdt.CvRDT

/**
 * Created by jackqack on 21/05/17.
 */


/**
 * Increment-decrement counter.
 */
class PNCounter : CvRDT<Int, PNCounter> {

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
     * Decrement counter by the given key.
     */
    fun decrement(key: String) {
        decrements.increment(key)
    }

    override fun merge(other: PNCounter) {
        increments.merge(other.increments)
        decrements.merge(other.decrements)
    }

    override fun value(): Int {
        return increments.value() - decrements.value()
    }

    override fun copy(): PNCounter {
        return PNCounter(this.increments.copy(), this.decrements.copy())
    }
}
