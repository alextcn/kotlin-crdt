package cvrdt.set

import cvrdt.CvRDT

/**
 * Created by jackqack on 21/05/17.
 */

internal interface CvRDTSet<V, T : CvRDTSet<V, T>> : CvRDT<MutableSet<V>, T> {

    fun add(x: V)

    fun addAll(elements: Collection<V>): Boolean

    fun contains(x: V) : Boolean

    fun remove(x: V) : Boolean

}