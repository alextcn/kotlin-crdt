package crdt.set

import crdt.CRDT

/**
 * Created by jackqack on 21/05/17.
 */

interface CRDTSet<V, T : CRDTSet<V, T>> : CRDT<MutableSet<V>, T> {

    fun add(x: V)

    fun addAll(elements: Collection<V>): Boolean

    fun contains(x: V) : Boolean

    fun remove(x: V) : Boolean

}