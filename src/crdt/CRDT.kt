package crdt

/**
 * Created by jackqack on 20/05/17.
 */

interface CRDT<V, T : CRDT<V, T>> {

    /**
     * Merge another CRDT into this one.
     * Method must be idempotent, commutative and associative.
     */
    fun merge(other: T)

    /**
     * Returns the immutable value of this CRDT.
     */
    fun value(): V

    /**
     * Create a copy of this CRDT.
     */
    fun copy(): T

}
