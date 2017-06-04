package cmrdt.set

import cmrdt.CmRDT

/**
 * Created by jackqack on 04/06/17.
 */

abstract class CmRDTSet<V, O : SetOperation<V>, T : CmRDTSet<V, O, T>> : CmRDT<MutableSet<V>, O, T> {


    override fun upgrade(op: O) {
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