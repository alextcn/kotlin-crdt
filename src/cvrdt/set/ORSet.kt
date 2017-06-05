package cvrdt.set

import java.util.*

/**
 * Created by jackqack on 02/06/17.
 */

internal class ORSet<V> : CvRDTSet<V, ORSet<V>> {

    private data class Sets(val adds: MutableSet<String> = HashSet(),
                            val removes: MutableSet<String> = HashSet())

    private val map: MutableMap<V, Sets> = HashMap()

    constructor() {
    }

    private constructor(map: MutableMap<V, Sets>) {
        for ((key, value) in map) {
            this.map[key] = Sets(HashSet(value.adds), HashSet(value.removes))
        }
    }


    override fun add(x: V) {
        if (!map.containsKey(x)) {
            map[x] = Sets()
        }
        map[x]!!.adds.add(UUID.randomUUID().toString())
    }

    override fun addAll(elements: Collection<V>): Boolean {
        for (x in elements) add(x)
        return elements.isNotEmpty()
    }

    override fun contains(x: V): Boolean {
        if (map.containsKey(x)) {
            val sets = map[x]!!
            return sets.adds.size > sets.removes.size
                    || !sets.removes.containsAll(sets.adds)
        } else return false
    }

    override fun remove(x: V): Boolean {
        if (map.containsKey(x)) {
            val sets = map[x]!!
            val oldRemovesSize = sets.removes.size
            sets.removes.addAll(sets.adds)
            return sets.removes.size > oldRemovesSize
        } else return false
    }


    override fun merge(other: ORSet<V>) {
        for ((x, sets) in other.map) {
            if (!map.containsKey(x)) {
                map[x] = Sets()
            }
            map[x]!!.adds.addAll(sets.adds)
            map[x]!!.removes.addAll(sets.removes)
        }
    }

    override fun value(): MutableSet<V> {
        val set = HashSet<V>()
        for ((key, value) in map) {
            if (contains(key)) set.add(key)
        }
        return set
    }

    override fun copy(): ORSet<V> {
        return ORSet(map)
    }


}