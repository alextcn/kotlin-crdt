package cmrdt.set.operation

import cmrdt.Operation

/**
 * Created by jackqack on 04/06/17.
 */

open class SetOperation<out V>(val type: Type, val x: V) : Operation {

    enum class Type {
        ADD, REMOVE
    }

}