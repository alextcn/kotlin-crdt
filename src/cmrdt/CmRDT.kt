package cmrdt


/**
 * Created by jackqack on 03/06/17.
 */

internal abstract class CmRDT<V, O : Operation, T : CmRDT<V, O, T>> {

    /**
     * After any changes in local CmRDT replica each operation
     * must be transmitted to all replicas. This listener
     * is called after local changes with an operation which should be send.
     * Operation must be commutative.
     */
    private var onDownstream: ((O) -> Unit)?


    constructor(onDownstream: ((O) -> Unit)? = null) {
        this.onDownstream = onDownstream
    }


    /**
     * Upgrade CmRDT replica by given operation received from
     * another replica.
     * Operation must be commutative.
     */
    abstract fun upgrade(op: O)

    /**
     * Returns the immutable value of this CmRDT.
     */
    abstract fun value(): V

    /**
     * Create a copy of this CmRDT.
     */
    abstract fun copy(): T

}
