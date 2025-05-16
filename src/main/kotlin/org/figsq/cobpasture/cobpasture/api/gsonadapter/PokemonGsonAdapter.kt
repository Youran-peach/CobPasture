package org.figsq.cobpasture.cobpasture.api.gsonadapter

import com.cobblemon.mod.common.pokemon.Pokemon
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.core.RegistryAccess
import java.lang.reflect.Type

object PokemonGsonAdapter : GsonAdapter<Pokemon> {
    override fun serialize(
        src: Pokemon,
        typeOfSrc: Type,
        context: JsonSerializationContext,
    ): JsonElement {
        return src.saveToJSON(RegistryAccess.EMPTY, JsonObject())
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext,
    ): Pokemon {
        return Pokemon.loadFromJSON(RegistryAccess.EMPTY, json.asJsonObject)
    }
}