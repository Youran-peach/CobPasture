package org.figsq.cobpasture.cobpasture.api

import com.cobblemon.mod.common.pokemon.Pokemon
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.figsq.cobpasture.cobpasture.api.breedlogic.BreedLogicManager
import org.figsq.cobpasture.cobpasture.api.events.PastureMakeEggEvent
import org.figsq.cobpasture.cobpasture.gui.PastureGui

class Pasture {
    var parent1: Pokemon? = null
        set(value) {
            field = value
            check()
        }

    var parent2: Pokemon? = null
        set(value) {
            field = value
            check()
        }

    var egg: Pokemon? = null
        set(value) {
            field = value
            check()
        }

    //tick < 0 则会跳过检测
    var tick = -1L

    fun makeEgg() {
        if (this.egg != null) return
        if (parent1 != null && parent2 != null) {
            //事件触发
            val event = PastureMakeEggEvent(
                this,
                BreedLogicManager.breedLogic.makeEggPokemon(parent1!!, parent2!!)
            )
            Bukkit.getServer().pluginManager.callEvent(event)
            if (event.isCancelled) return
            //获取事件修改结果
            this.egg = event.egg


            //TODO 产出后执行(可以没有)
            return
        }
    }

    /**
     * 检查 父母 宝可梦 和 蛋 宝可梦数据后判断是否设置成检测还是不检测的状态(tick = -1L or 0L)
     */
    fun check() {
        if (parent1 != null && parent2 != null && this.egg == null &&
            BreedLogicManager.breedLogic.canBreed(
                parent1!!,
                parent2!!
            )
        ) {
            //检查
            this.tick = 0L
            return
        }
        //不检查
        this.tick = -1L
    }

    /**
     * 是否被 DataManager.unifiedPastureDetector 检测
     */
    fun needCheck(): Boolean {
        return this.tick > -1
    }

    /**
     * 玩家打开临时容器(多个玩家同时打开大概率出毛病)
     */
    fun open(player: Player) {
        player.openInventory(PastureGui(this).inventory)
    }

    //没有打算写tick方法 所有检测都是在 DataManager.unifiedPastureDetector 中完成
}