package cmrdt.set

import cmrdt.set.operation.ORSetOperation
import cmrdt.set.operation.SetOperation
import java.util.*
import kotlin.collections.HashSet

/**
 * Created by jackqack on 04/06/17.
 */

internal class ORSet<V> : CmRDTSet<V, ORSetOperation<V>, ORSet<V>> {

    private val map: MutableMap<V, MutableSet<String>> = HashMap()


    constructor(onDownstream: ((SetOperation<V>) -> Unit)? = null) : super(onDownstream)

    private constructor(set: MutableMap<V, MutableSet<String>>) {
        for ((key, value) in set) map.put(key, HashSet(value))
    }


    override fun add(x: V) {
        if (!map.containsKey(x)) {
            map[x] = HashSet()
        }
        val tag = UUID.randomUUID().toString()
        map[x]!!.add(tag)
        onDownstream(ORSetOperation(SetOperation.Type.ADD, x, listOf(tag)))
    }

    override fun addAll(elements: Collection<V>) {
        for (x in elements) add(x)
    }

    override fun contains(x: V): Boolean {
        return map.containsKey(x)
    }

    override fun remove(x: V): Boolean {
        if (map.containsKey(x)) {
            val tags = map.remove(x)!!
            onDownstream(ORSetOperation(SetOperation.Type.REMOVE, x, tags))
            return true
        }
        return false
    }


    override fun upgrade(op: ORSetOperation<V>) {
        when (op.type) {
            SetOperation.Type.ADD -> {
                addTags(op.x, op.tags)
            }
            SetOperation.Type.REMOVE -> {
                removeTags(op.x, op.tags)
            }
        }
    }

    private fun addTags(x: V, tags: Collection<String>) {
        if (!map.containsKey(x)) {
            map[x] = HashSet()
        }
        map[x]!!.addAll(tags)
    }

    private fun removeTags(x: V, tags: Collection<String>) {
        if (map.containsKey(x)) {
            map[x]!!.removeAll(tags)
            if (map[x]!!.isEmpty()) map.remove(x)
        }
    }


    override fun value(): MutableSet<V> {
        return HashSet(map.keys)
    }

    override fun copy(): ORSet<V> {
        return ORSet(map)
    }
}