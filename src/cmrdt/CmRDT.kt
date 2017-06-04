package cmrdt


/**
 * Created by jackqack on 03/06/17.
 */

internal abstract class CmRDT<V, O : Operation, T : CmRDT<V, O, T>>(var downstreamListener: IDownstreamListener<O>? = null) {


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
