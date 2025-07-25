package org.figsq.cobpasture.cobpasture.api.breedlogic.genetic

import com.cobblemon.mod.common.pokemon.Pokemon

interface IGeneticHandler {
    fun handle(parent1: Pokemon, parent2: Pokemon, egg: Pokemon);
}
