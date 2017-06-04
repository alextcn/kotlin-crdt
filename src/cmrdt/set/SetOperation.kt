package cmrdt.set

import cmrdt.Operation

/**
 * Created by jackqack on 04/06/17.
 */

abstract class SetOperation<V> : Operation {

    val x: V

    protected constructor(x: V) {
        this.x = x
    }

}