package cmrdt.set.operation


/**
 * Created by jackqack on 04/06/17.
 */

class ORSetOperation<out V>(type: Type, x: V, val tags: Collection<String>) : SetOperation<V>(type, x)