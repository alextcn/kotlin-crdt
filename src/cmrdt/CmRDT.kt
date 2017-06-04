package cmrdt


/**
 * Created by jackqack on 03/06/17.
 */

internal interface CmRDT<V, O : Operation, T : CmRDT<V, O, T>> {

    /**
     * Upgrade CmRDT replica by given operation received from
     * another replica.
     * Operation must be commutative.
     */
    fun upgrade(op: O)

    /**
     * After any changes in local CmRDT replica each operation
     * must be transmitted to all replicas. This method
     * is called after local changes with an operation which should be send.
     * Operation must be commutative.
     */
    fun downstream(op: O)

    /**
     * Returns the immutable value of this CmRDT.
     */
    fun value(): V

    /**
     * Create a copy of this CmRDT.
     */
    fun copy(): T

}
