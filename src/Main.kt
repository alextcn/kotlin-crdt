import crdt.counter.GCounter
import crdt.counter.PNCounter

/**
 * Created by jackqack on 20/05/17.
 */


fun testGCounter() {
    val replica1 = GCounter()
    replica1.increment("hostname1")
    replica1.increment("hostname2")
    replica1.increment("hostname2")
    replica1.increment("hostname2")
    replica1.increment("hostname3")
    println(replica1.value())

    val replica2 = GCounter()
    replica2.increment("hostname1")
    replica2.increment("hostname1")
    replica2.increment("hostname2")
    replica2.increment("hostname2")
    replica2.increment("hostname3")
    println(replica2.value())

    replica1.merge(replica2)
    println(replica1.value())

}

fun testPNCounter() {

    val replica1 = PNCounter()
    // hostname1 = -1
    replica1.increment("hostname1")
    replica1.decrement("hostname1")
    replica1.decrement("hostname1")

    // hostname2 = 2
    replica1.decrement("hostname2")
    replica1.increment("hostname2")
    replica1.increment("hostname2")
    replica1.increment("hostname2")

    // hostname3 = 0
    replica1.decrement("hostname3")
    replica1.increment("hostname3")
    replica1.decrement("hostname3")
    replica1.increment("hostname3")

    // sum = 1
    println(replica1.value())


    val replica2 = PNCounter()
    // hostname1 = 2
    replica2.increment("hostname1")
    replica2.increment("hostname1")
    // hostname2 = -2
    replica2.decrement("hostname2")
    replica2.decrement("hostname2")
    // hostname3 = 3
    replica2.increment("hostname3")
    replica2.increment("hostname3")
    replica2.increment("hostname3")
    // sum = 3
    println(replica2.value())

    replica1.merge(replica2)
    // merge = 2
    println(replica1.value())

}

fun main(args: Array<String>) {

    testPNCounter()

}