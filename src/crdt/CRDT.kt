package crdt

/**
 * Created by jackqack on 20/05/17.
 */

interface CRDT<T : CRDT<T>> {

    /**
     * Merge another CRDT into this one.
     * Method must be idempotent, commutative and associative.
     */
    fun merge(other: T)

    /**
     * Create a copy of this CRDT.
     */
    fun copy(): T

}
