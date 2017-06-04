package cmrdt.set.operation

import cmrdt.Operation

/**
 * Created by jackqack on 04/06/17.
 */

internal abstract class SetOperation<V> : Operation {

    val x: V

    protected constructor(x: V) {
        this.x = x
    }

}