package org.figsq.cobpasture.cobpasture.api.breedlogic.genetic

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager

/**
 * 参考： https://wiki.52poke.com/wiki/%E5%AE%9D%E5%8F%AF%E6%A2%A6%E5%9F%B9%E8%82%B2#%E6%80%A7%E6%A0%BC
 * 只实现了不变之石
 */
object NatureGenetic : IGeneticHandler {
    override fun handle(
        parent1: Pokemon,
        parent2: Pokemon,
        egg: Pokemon
    ) {
        hasEverstone(parent1, parent2)?.let {
            egg.nature = it.nature
        }
    }

    fun hasEverstone(parent1: Pokemon, parent2: Pokemon): Pokemon? {
        if ("everstone" == CobblemonHeldItemManager.showdownId(parent1.heldItem())) return parent1
        if ("everstone" == CobblemonHeldItemManager.showdownId(parent2.heldItem())) return parent2
        return null
    }
}
