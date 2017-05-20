package crdt.counter

import java.util.*

/**
 * Created by jackqack on 20/05/17.
 */


class GCounter {

    val counts: MutableMap<String, Int> = HashMap()


    /**
     * Increment counter by the given key.
     */
    fun increment(key: String) {
        counts[key] = (counts[key] ?: 0) + 1
    }

    /**
     * Merge another CRDT into this one.
     * Method must be idempotent, commutative and associative.
     */
    fun merge(other: GCounter) {
        for ((key, value) in other.counts) {
            counts[key] = Math.max(counts[key] ?: 0, value)
        }
    }

    /**
     * Returns the immutable value of this CRDT.
     */
    fun value(): Int {
        var sum = 0
        for ((key, value) in counts) {
            sum += value
        }
        return sum
    }

}