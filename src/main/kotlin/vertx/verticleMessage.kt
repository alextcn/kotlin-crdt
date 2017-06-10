package vertx

import cmrdt.set.operation.ORSetOperation
import cmrdt.set.operation.SetOperation
import io.vertx.core.json.JsonObject
import vertx.VerticleMessage.Companion.FIELD_OP_TAGS
import vertx.VerticleMessage.Companion.FIELD_OP_TYPE
import vertx.VerticleMessage.Companion.FIELD_OP_VALUE

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

        const val FIELD_OP_TYPE = "type"
        const val FIELD_OP_VALUE = "value"
        const val FIELD_OP_TAGS = "tags"
    }

    fun toJson(): JsonObject {
        val json = JsonObject()
        json.put(FIELD_TYPE, type.name)
        json.put(FIELD_DATA, data)
        return json
    }

}


private fun fromJson(json: JsonObject): VerticleMessage? {
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

private fun operationFromJson(json: JsonObject): ORSetOperation<Int>? {
    if (!json.isEmpty && json.containsKey(FIELD_OP_TYPE) && json.containsKey(FIELD_OP_VALUE) &&
            json.containsKey(FIELD_OP_TAGS)) {

        val type: SetOperation.Type
        try {
            type = SetOperation.Type.valueOf(json.getString(FIELD_OP_TYPE))
        } catch (e: IllegalArgumentException) {
            return null
        }
        val value = json.getInteger(FIELD_OP_VALUE) ?: return null
        val tags = ArrayList<String>()
        val jsonTags = json.getJsonArray(FIELD_OP_TAGS) ?: return null
        for ((i, _) in jsonTags.withIndex()) {
            val s = jsonTags.getString(i) ?: return null
            tags.add(s)
        }

        return ORSetOperation(type, value, tags)

    } else return null
}

private fun stateFromJson(json: JsonObject): MutableMap<Int, MutableSet<String>>? {
    if (!json.isEmpty) {
        val map = HashMap<Int, MutableSet<String>>()
        for (str in json.fieldNames()) {
            try {
                val tags = HashSet<String>()
                map.put(str.toInt(), tags)
                val jsonTags = json.getJsonArray(str) ?: return null
                for ((i, _) in jsonTags.withIndex()) {
                    val s = jsonTags.getString(i) ?: return null
                    tags.add(s)
                }
            } catch (e: NumberFormatException) {
                return null
            }
        }
        return map
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

fun parseState(str: String): MutableMap<Int, MutableSet<String>>? {
    try {
        val json = JsonObject(str)
        return stateFromJson(json)
    } catch (e: Exception) {
        return null
    }
}

fun parseOperation(str: String): ORSetOperation<Int>? {
    try {
        val json = JsonObject(str)
        return operationFromJson(json)
    } catch (e: Exception) {
        return null
    }
}

fun toJson(op: ORSetOperation<Int>): JsonObject {
    return JsonObject.mapFrom(op)
}
