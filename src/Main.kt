import crdt.counter.GCounter

/**
 * Created by jackqack on 20/05/17.
 */



fun main(args: Array<String>) {
    println("123")

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