package cvrdt.counter

import cvrdt.CvRDT
import java.util.*

/**
 * Created by jackqack on 20/05/17.
 */

/**
 * Grow-only counter.
 */
internal class GCounter : CvRDT<Int, GCounter> {

    private val counts: MutableMap<String, Int> = HashMap()


    /**
     * Increment counter by the given key.
     */
    fun increment(key: String) {
        counts[key] = (counts[key] ?: 0) + 1
    }


    override fun merge(other: GCounter) {
        for ((key, value) in other.counts) {
            counts[key] = Math.max(counts[key] ?: 0, value)
        }
    }

    override fun value(): Int {
        var sum = 0
        for ((key, value) in counts) {
            sum += value
        }
        return sum
    }

    override fun copy(): GCounter {
        val copy = GCounter()
        copy.counts.putAll(this.counts)
        return copy
    }


}