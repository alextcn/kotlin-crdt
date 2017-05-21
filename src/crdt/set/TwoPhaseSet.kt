package crdt.set

import java.util.*

/**
 * Created by jackqack on 21/05/17.
 */

/**
 * Two-phase set.
 */
internal class TwoPhaseSet<V> : CRDTSet<V, TwoPhaseSet<V>> {

    private val added: GSet<V>
    private val tombstone: GSet<V>

    constructor() {
        added = GSet()
        tombstone = GSet()
    }

    private constructor(added: GSet<V>, tombstone: GSet<V>) {
        this.added = added.copy()
        this.tombstone = tombstone.copy()
    }

    override fun add(x: V) {
        if (!tombstone.contains(x)) added.add(x)
    }

    override fun addAll(elements: Collection<V>): Boolean {
        val filtered = elements.filter { !tombstone.contains(it) }
        return added.addAll(filtered)
    }

    override fun contains(x: V): Boolean {
        return !tombstone.contains(x) && added.contains(x)
    }

    fun remove(x: V): Boolean {
        if (added.contains(x)) {
            tombstone.add(x)
            return true
        } else return false
    }

    override fun merge(other: TwoPhaseSet<V>) {
        added.merge(other.added)
        tombstone.merge(other.tombstone)
    }

    override fun value(): MutableSet<V> {
        val s = added.value()
        s.removeAll(tombstone.value())
        return s
    }

    override fun copy(): TwoPhaseSet<V> {
        return TwoPhaseSet(added, tombstone)
    }
}