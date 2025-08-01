package org.figsq.cobpasture.cobpasture.api.breedlogic.genetic

import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.helditem.CobblemonHeldItemManager
import org.figsq.cobpasture.cobpasture.CobPasture
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
        whoHasDestinyKnot(parent1, parent2)?.let {
            val notGenetic = Stats.PERMANENT.random()
            tryConsumeKnot(pokemon = it)
            genetic5iv(parent1, parent2, egg, notGenetic)
        }
    }

    fun tryConsumeKnot(pokemon: Pokemon){
        if (!destinyknotConsume || !hasDestinyKnot(pokemon)) return
        val copy = pokemon.heldItem().copy()
        copy.count--
        pokemon.swapHeldItem(copy,false)
    }

    /**
     * 遗传5个个体从父母宝可梦随机，排除其中一个
     * @param notGenetic 排除的一项
     * @param parent1 父母宝可梦
     * @param parent2 父母宝可梦
     * @param target 目标宝可梦
     */
    fun genetic5iv(parent1: Pokemon, parent2: Pokemon, target: Pokemon, notGenetic: Stat) {
        for (stat in Stats.PERMANENT) {
            if (notGenetic == stat) continue
            target.setIV(stat, if (Random.nextBoolean()) parent1.ivs[stat]!! else parent2.ivs[stat]!!)
        }
    }

    /**
     * 返回是哪知得红线生效了，多个也只会有一个
     * @return 没有宝可梦拥有则返回空
     */
    fun whoHasDestinyKnot(parent1: Pokemon, parent2: Pokemon): Pokemon? {
        return if (hasDestinyKnot(parent1)) parent1 else
            if (hasDestinyKnot(parent2)) parent2 else null
    }

    fun hasDestinyKnot(pokemon: Pokemon): Boolean {
        return "destinyknot" == CobblemonHeldItemManager.showdownId(pokemon.heldItem())
    }

    //默认消耗
    var destinyknotConsume: Boolean = true

    //加载配置所需项
    fun load() {
        destinyknotConsume = CobPasture.INSTANCE.config.getBoolean("destinyknot-consume")
    }
}
