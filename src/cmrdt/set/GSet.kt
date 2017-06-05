package cmrdt.set

import cmrdt.set.operation.SetOperation
import java.util.*
import javax.naming.OperationNotSupportedException

/**
 * Created by jackqack on 04/06/17.
 */

internal class GSet<V> : CmRDTSet<V, SetOperation<V>, GSet<V>> {

    private val set: MutableSet<V> = HashSet()


    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)

    private constructor(set: MutableSet<V>) {
        this.set.addAll(set)
    }


    override fun add(x: V) {
        if (!set.contains(x)) {
            set.add(x)
            onDownstream(SetOperation(SetOperation.Type.ADD, x))
        }
    }

    override fun addAll(elements: Collection<V>) {
        for (x in elements) add(x)
    }

    override fun contains(x: V): Boolean {
        return set.contains(x)
    }

    override fun remove(x: V): Boolean {
        throw OperationNotSupportedException("GSet does not allows removes")
    }


    override fun upgrade(op: SetOperation<V>) {
        when (op.type) {
            SetOperation.Type.ADD -> {
                set.add(op.x)
            }
            SetOperation.Type.REMOVE -> {
                // ignore
            }
        }
    }


    override fun value(): MutableSet<V> {
        return HashSet(this.set)
    }

    override fun copy(): GSet<V> {
        return GSet(set)
    }

}