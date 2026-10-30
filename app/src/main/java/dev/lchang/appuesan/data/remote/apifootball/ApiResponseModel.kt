package dev.lchang.appuesan.data.remote.apifootball

import com.google.gson.JsonElement

data class Country(
    val name: String,
    val code: String?,
    val flag: String?
)
data class CountryResponse(
    // API-Sports devuelve HTTP 200 con "errors" ([] u objeto) cuando falla la key o el límite
    val errors: JsonElement?,
    val response: List<Country>
)
data class Team(
    val id: Int,
    val name: String,
    val code: String?,
    val logo: String,
    val country: String,
    val founded: Int?,
    val national: Boolean
)
data class TeamWrapper(
    val team: Team
)
data class TeamResponse(
    val errors: JsonElement?,
    val response: List<TeamWrapper>
)

// Convierte el campo "errors" en un mensaje legible, o null si no hay errores
fun JsonElement?.toErrorMessage(): String? {
    if (this == null || isJsonNull) return null
    if (isJsonArray) {
        val array = asJsonArray
        return if (array.size() == 0) null else array.joinToString { it.toString() }
    }
    if (isJsonObject) {
        val obj = asJsonObject
        if (obj.size() == 0) return null
        return obj.entrySet().joinToString { (_, value) ->
            if (value.isJsonPrimitive) value.asString else value.toString()
        }
    }
    return toString()
}
