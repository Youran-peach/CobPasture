package org.figsq.cobpasture.cobpasture.api.breedlogic.genetic

import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager
import kotlin.random.Random

/**
 * 能力遗传
 * 参考: https://wiki.52poke.com/wiki/%E5%AE%9D%E5%8F%AF%E6%A2%A6%E5%9F%B9%E8%82%B2#%E8%83%BD%E5%8A%9B
 * #只实现了红线逻辑!
 * TODO 六代前的还没写
 */
object IVGenetic : IGeneticHandler {
    override fun handle(
        parent1: Pokemon,
        parent2: Pokemon,
        egg: Pokemon
    ) {
        if (!hasDestinyKnot(parent1, parent2)) return
        val notGenetic = Stats.PERMANENT.random()
        for (stat in Stats.PERMANENT) {
            if (notGenetic == stat) continue
            egg.setIV(stat, if (Random.nextBoolean()) parent1.ivs[stat]!! else parent2.ivs[stat]!!)
        }
    }


    fun hasDestinyKnot(parent1: Pokemon, parent2: Pokemon): Boolean {
        val showdownId = CobblemonHeldItemManager.showdownId(parent1.heldItem())
        println(showdownId)
        return "destinyknot" == showdownId ||
                "destinyknot" == CobblemonHeldItemManager.showdownId(parent2.heldItem())
    }
}
