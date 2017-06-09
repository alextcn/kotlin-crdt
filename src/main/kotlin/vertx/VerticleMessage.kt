package vertx

import cmrdt.set.operation.ORSetOperation
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject

/**
 * Created by jackqack on 09/06/17.
 */


enum class MessageType {
    REQUEST_STATE, RESPONSE_STATE, SEND_OPERATION
}

data class VerticleMessage(val type: MessageType, val data: String? = null) {

    companion object {
        const val FIELD_TYPE = "type"
        const val FIELD_DATA = "data"
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.put(FIELD_TYPE, type.name)
        json.put(FIELD_DATA, data)
        return json
    }

}


fun fromJson(json: JsonObject): VerticleMessage? {
    if (!json.isEmpty && json.containsKey(VerticleMessage.FIELD_TYPE)) {
        when (json.getString(VerticleMessage.FIELD_TYPE)) {
            MessageType.REQUEST_STATE.name -> {
                return VerticleMessage(MessageType.REQUEST_STATE)
            }
            MessageType.REQUEST_STATE.name -> {
                return VerticleMessage(MessageType.RESPONSE_STATE, json.getString(VerticleMessage.FIELD_DATA))
            }
            MessageType.SEND_OPERATION.name -> {
                if (json.containsKey(VerticleMessage.FIELD_DATA)) {
                    return VerticleMessage(MessageType.SEND_OPERATION, json.getString(VerticleMessage.FIELD_DATA))
                }
            }
        }
        return null
    } else return null
}

fun parseMessage(str: String): VerticleMessage? {
    try {
        val json = JsonObject(str)
        return fromJson(json)
    } catch (e: Exception) {
        return null
    }
}

fun <T> parseState(str: String): MutableMap<T, MutableSet<String>>? {
    // TODO: implement
    return null
}

fun <T> parseOperation(str: String): ORSetOperation<T>? {
    // TODO: implement
    return null
}

fun toJson(op: ORSetOperation<Int>) : JsonObject {
    // TODO: implement
    return JsonObject()
}
