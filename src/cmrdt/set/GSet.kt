package cmrdt.set

import cmrdt.set.operation.SetOperation
import java.util.*

/**
 * Created by jackqack on 04/06/17.
 */

internal class GSet<V> : CmRDTSet<V, GSet<V>> {

    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)


    private val set: MutableSet<V> = HashSet()


    override fun add(x: V) {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addAll(elements: Collection<V>): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun contains(x: V): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(x: V): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun value(): MutableSet<V> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun copy(): GSet<V> {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}