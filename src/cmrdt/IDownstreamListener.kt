package cmrdt

import com.sun.java.swing.action.OpenAction

/**
 * Created by jackqack on 04/06/17.
 */

/**
 * After any changes in local CmRDT replica each operation
 * must be transmitted to all replicas. This listener
 * is called after local changes with an operation which should be send.
 * Operation must be commutative.
 */
internal interface IDownstreamListener<in O : Operation> {
    fun onDownstream(op: O)
}