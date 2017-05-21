package crdt.set

import crdt.CRDT

/**
 * Created by jackqack on 21/05/17.
 */

interface CRDTSet<V, T : CRDTSet<V, T>> : CRDT<Set<V>, T>{

}