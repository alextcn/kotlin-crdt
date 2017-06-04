package cmrdt.set

import cmrdt.CmRDT
import cmrdt.set.operation.SetOperation

/**
 * Created by jackqack on 04/06/17.
 */

internal abstract class CmRDTSet<V, T : CmRDTSet<V, T>> : CmRDT<MutableSet<V>, SetOperation<V>, T> {

    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)


    abstract fun add(x: V, withDownstream: Boolean = true)

    abstract fun addAll(elements: Collection<V>, withDownstream: Boolean = true)

    abstract fun contains(x: V): Boolean

    abstract fun remove(x: V, withDownstream: Boolean = true): Boolean

}