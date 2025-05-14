package org.figsq.cobpasture.cobpasture.api.gsonadapter

import com.google.gson.JsonArray
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import org.figsq.cobpasture.cobpasture.api.Pasture
import org.figsq.cobpasture.cobpasture.api.playerdata.PlayerData
import java.lang.reflect.Type
import java.util.UUID

object PlayerDataGsonAdapter : GsonAdapter<PlayerData> {
    override fun serialize(
        src: PlayerData,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonObject().apply {
            this.addProperty("uuid", src.uuid.toString())
            this.add("pastures", JsonArray().apply {
                for (pasture in src.pastures) this.add(GsonHelper.GSON.toJsonTree(pasture))
            })
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PlayerData {
        val jsonObject = json.asJsonObject
        return PlayerData(
            uuid = UUID.fromString(jsonObject.get("uuid").asString),
        ).apply {
            for (element in jsonObject.get("pastures").asJsonArray)
                this.pastures.add(GsonHelper.GSON.fromJson(element, Pasture::class.java))
        }
    }
}