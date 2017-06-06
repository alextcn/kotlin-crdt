package vertx

/**
 * Created by jackqack on 06/06/17.
 */


import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.ext.web.Router


class VerticleNode : AbstractVerticle() {

    override fun start(startFuture: Future<Void>?) {
        val router = createRouter()

        vertx.createHttpServer()
                .requestHandler { router.accept(it) }
                .listen(config().getInteger("http.port", 8080)) { result ->
                    if (result.succeeded()) {
                        startFuture?.complete()
                    } else {
                        startFuture?.fail(result.cause())
                    }
                }
    }

    private fun createRouter() = Router.router(vertx).apply {
        get("/").handler({ it.response().end("Welcome!") })
        get("/islands").handler({ it.response().end("Islands") })
    }

    //
}