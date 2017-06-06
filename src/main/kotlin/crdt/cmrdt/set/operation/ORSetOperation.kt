package cmrdt.set.operation

import cmrdt.Operation

/**
 * Created by jackqack on 04/06/17.
 */

internal class ORSetOperation<out V>(type: Type, x: V, val tags: Collection<String>) : SetOperation<V>(type, x)