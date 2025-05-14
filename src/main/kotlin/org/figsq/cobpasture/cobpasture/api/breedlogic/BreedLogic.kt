package org.figsq.cobpasture.cobpasture.api.breedlogic

import com.cobblemon.mod.common.pokemon.Pokemon

interface BreedLogic {
    fun makeEggPokemon(parent1: Pokemon, parent2: Pokemon): Pokemon? {
        if (canBreed(parent1, parent2)) return makeEgg(parent1, parent2)
        return null
    }

    /**
     * @param parent1 父母宝可梦
     * @param parent2 父母宝可梦
     * @return 父母产出的蛋，如果无法产出则会是null
     */
    fun makeEgg(parent1: Pokemon, parent2: Pokemon): Pokemon?

    fun canBreed(parent1: Pokemon, parent2: Pokemon): Boolean
}