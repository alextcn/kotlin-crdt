package vertx

/**
 * Created by jackqack on 06/06/17.
 */


import io.vertx.core.AbstractVerticle
import io.vertx.core.Future


class VerticleNode : AbstractVerticle() {

    companion object {
        const val PORT = 10390
    }


    override fun start(startFuture: Future<Void>?) {
        println("start server")
        val server = vertx.createNetServer()

        // this handler is called whenever a new TCP connection is created by another node
        server.connectHandler {
            println("New incoming TCP connection!")
            it.handler({
                println("incoming data (${it.length()} bytes): ${it.getString(0, it.length())}")
            })
        }

        server.listen(PORT) { result ->
            if (result.succeeded()) {
                println("server started")
                startFuture?.complete()
            } else {
                println("server failed: ${result.cause()}")
                startFuture?.fail(result.cause())
            }
        }
    }

}