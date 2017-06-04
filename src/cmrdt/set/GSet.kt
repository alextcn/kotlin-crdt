package cmrdt.set

import cmrdt.set.operation.SetOperation
import java.util.*
import javax.naming.OperationNotSupportedException

/**
 * Created by jackqack on 04/06/17.
 */

internal class GSet<V> : CmRDTSet<V, GSet<V>> {

    private val set: MutableSet<V> = HashSet()


    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)

    private constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null, set: MutableSet<V>) : super(onDownstream) {
        this.set.addAll(set)
    }


    override fun add(x: V, withDownstream: Boolean) {
        if (!set.contains(x)) {
            set.add(x)
            if (withDownstream) onDownstream(SetOperation(SetOperation.Type.ADD, x))
        }
    }

    override fun addAll(elements: Collection<V>, withDownstream: Boolean) {
        for (x in elements) {
            this.add(x, withDownstream)
        }
    }

    override fun contains(x: V): Boolean {
        return set.contains(x)
    }

    override fun remove(x: V, withDownstream: Boolean): Boolean {
        throw OperationNotSupportedException("GSet does not allows removes")
    }


    override fun upgrade(op: SetOperation<V>) {
        when (op.type) {
            SetOperation.Type.ADD -> {
                this.add(op.x, false)
            }
            SetOperation.Type.REMOVE -> {
                this.remove(op.x, false)
            }
        }
    }


    override fun value(): MutableSet<V> {
        return HashSet(this.set)
    }

    override fun copy(): GSet<V> {
        return GSet(onDownstream, set)
    }

}