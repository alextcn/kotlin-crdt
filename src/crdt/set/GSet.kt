package crdt.set

import java.util.*

/**
 * Created by jackqack on 21/05/17.
 */

/**
 * Grow-only set.
 */
internal class GSet<T> : CRDTSet<T, GSet<T>> {

    private val set: MutableSet<T> = HashSet()

    constructor() {
    }

    private constructor(set: Set<T>) {
        this.set.addAll(set)
    }


    fun add(x: T) {
        set.add(x)
    }

    fun addAll(elements: Collection<T>): Boolean {
        return set.addAll(elements)
    }

    fun contains(x: T): Boolean {
        return set.contains(x)
    }


    override fun merge(other: GSet<T>) {
        set.addAll(other.set)
    }

    override fun value(): Set<T> {
        return HashSet<T>(this.set)
    }

    override fun copy(): GSet<T> {
        return GSet(this.set)
    }

}