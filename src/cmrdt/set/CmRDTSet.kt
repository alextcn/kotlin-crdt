package cmrdt.set

import cmrdt.CmRDT
import cmrdt.set.operation.AddOp
import cmrdt.set.operation.RemoveOp
import cmrdt.set.operation.SetOperation

/**
 * Created by jackqack on 04/06/17.
 */

internal abstract class CmRDTSet<V, T : CmRDTSet<V, T>> : CmRDT<MutableSet<V>, SetOperation<V>, T> {

    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)


    override fun upgrade(op: SetOperation<V>) {
        if (op is AddOp<*>) {
            this.add(op.x)
        } else if (op is RemoveOp<*>) {
            this.remove(op.x)
        }
    }

    abstract fun add(x: V)

    abstract fun addAll(elements: Collection<V>): Boolean

    abstract fun contains(x: V): Boolean

    abstract fun remove(x: V): Boolean

}