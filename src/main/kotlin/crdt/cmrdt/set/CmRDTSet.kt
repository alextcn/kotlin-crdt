package cmrdt.set

import cmrdt.CmRDT
import cmrdt.set.operation.SetOperation

/**
 * Created by jackqack on 04/06/17.
 */

abstract class CmRDTSet<V, O : SetOperation<V>, T : CmRDTSet<V, O, T>>(
        onDownstream: (O) -> Unit
) : CmRDT<MutableSet<V>, O, T>(onDownstream) {


    abstract fun add(x: V)

    abstract fun addAll(elements: Collection<V>)

    abstract fun contains(x: V): Boolean

    abstract fun remove(x: V): Boolean

}