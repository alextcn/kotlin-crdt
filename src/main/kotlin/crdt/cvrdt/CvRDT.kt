package cvrdt

/**
 * Created by jackqack on 20/05/17.
 */

interface CvRDT<V, T : CvRDT<V, T>> {

    /**
     * Merge another CvRDT into this one.
     * Method must be idempotent, commutative and associative.
     */
    fun merge(other: T)

    /**
     * Returns the immutable value of this CvRDT.
     */
    fun value(): V

    /**
     * Create a copy of this CvRDT.
     */
    fun copy(): T

}
