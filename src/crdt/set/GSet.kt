package crdt.set

import java.util.*
import javax.naming.OperationNotSupportedException

/**
 * Created by jackqack on 21/05/17.
 */

/**
 * Grow-only set.
 */
internal class GSet<V> : CRDTSet<V, GSet<V>> {

    private val set: MutableSet<V> = HashSet()

    constructor() {
    }

    private constructor(set: MutableSet<V>) {
        this.set.addAll(set)
    }


    override fun add(x: V) {
        set.add(x)
    }

    override fun addAll(elements: Collection<V>): Boolean {
        return set.addAll(elements)
    }

    override fun contains(x: V): Boolean {
        return set.contains(x)
    }

    override fun remove(x: V): Boolean {
        throw OperationNotSupportedException("GSet does not allows removes")
    }

    override fun merge(other: GSet<V>) {
        set.addAll(other.set)
    }

    override fun value(): MutableSet<V> {
        return HashSet(this.set)
    }

    override fun copy(): GSet<V> {
        return GSet(this.set)
    }

}