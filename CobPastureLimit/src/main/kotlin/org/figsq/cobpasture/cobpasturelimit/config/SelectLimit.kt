package org.figsq.cobpasture.cobpasturelimit.config

import com.cobblemon.mod.common.pokemon.OriginalTrainerType
import com.cobblemon.mod.common.pokemon.Pokemon
import org.figsq.cobpasture.cobpasture.CobPasture
import org.figsq.cobpasture.cobpasturelimit.CobPastureLimit
import java.util.*

object SelectLimit {
    var ORIGINAL_TRAINER_ENABLE: Boolean = false
    var ORIGINAL_TRAINER_DEFAULT: Boolean = false
    var pokemons = mutableListOf<String>()
    var friendship = 0
    var level = 0
    lateinit var tips: String

    fun load() {
        val config = CobPastureLimit.INSTANCE.config
        ORIGINAL_TRAINER_ENABLE = config.getBoolean("select-limit.original-trainer.enable")
        ORIGINAL_TRAINER_DEFAULT = config.getBoolean("select-limit.original-trainer.default")
        pokemons = config.getStringList("select-limit.pokemons").toMutableList()
        friendship = config.getInt("select-limit.friendship")
        level = config.getInt("select-limit.level")
        tips = config.getString("select-limit.tips")!!
    }

    fun isLimit(pokemon: Pokemon): Boolean {
        return (ORIGINAL_TRAINER_ENABLE && pokemon.originalTrainer?.let {
            if (pokemon.originalTrainerType == OriginalTrainerType.PLAYER) UUID.fromString(
                pokemon.originalTrainer
            ) == pokemon.getOwnerUUID() else null
        } ?: ORIGINAL_TRAINER_DEFAULT) ||
                (pokemons.contains(pokemon.species.name) || pokemons.contains(pokemon.getDisplayName().string)) ||
                pokemon.friendship < friendship
    }
}