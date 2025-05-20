package org.figsq.cobpasture.cobpasturelimit.config

import com.cobblemon.mod.common.pokemon.Pokemon
import org.figsq.cobpasture.cobpasturelimit.CobPastureLimit

object MakeEggLimit {
    var INSTANT_TAG_ORIGINAL_TRAINER = false

    lateinit var pokemons: List<String>

    fun load() {
        val config = CobPastureLimit.INSTANCE.config
        val section = config.getConfigurationSection("make-egg-limit")!!
        INSTANT_TAG_ORIGINAL_TRAINER = section.getBoolean("instant-tag-original-trainer")
        pokemons = section.getStringList("pokemons")
    }

    fun isLimit(pokemon: Pokemon): Boolean {
        return pokemons.contains(pokemon.getDisplayName().string) || pokemons.contains(pokemon.species.name)
    }
}