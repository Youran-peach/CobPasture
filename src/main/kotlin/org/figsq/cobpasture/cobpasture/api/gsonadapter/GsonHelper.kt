package org.figsq.cobpasture.cobpasture.api.gsonadapter

import com.cobblemon.mod.common.pokemon.Pokemon
import com.google.gson.GsonBuilder
import org.figsq.cobpasture.cobpasture.api.playerdata.PlayerData

object GsonHelper {
    val GSON = GsonBuilder()
        .registerTypeAdapter(Pokemon::class.java, PokemonGsonAdapter)
        .registerTypeAdapter(PlayerData::class.java, PlayerDataGsonAdapter)
        .create()
}