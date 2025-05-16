package org.figsq.cobpasture.cobpasture

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import org.bukkit.Bukkit
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitTask
import org.figsq.cobpasture.cobpasture.api.EggHelper

//一些不知道放哪里的事件处理
object ListenerBase : Listener {
    /**
     * 一个定时检测玩家物品栏上宝可梦蛋的自动任务
     */
    private var autoDetectEggTask: BukkitTask? = null

    fun load() {
        autoDetectEggTask?.let {
            try {
                autoDetectEggTask!!.cancel()
            } catch (_: Exception) {
            }
        }

        val instance = CobPasture.INSTANCE
        val time = instance.config.getLong("incubation-check-time")
        val itime = instance.config.getLong("incubation-time")
        autoDetectEggTask = Bukkit.getScheduler().runTaskTimer(instance, Runnable {
            for (player in Bukkit.getOnlinePlayers()) {
                //最大检测数
                var max = 0
                for (i in 0..8) {
                    //限制最大到6
                    if (max >= 6) continue
                    player.inventory.getItem(i)?.let { item ->
                        EggHelper.getPokemonFromEgg(item)?.let {
                            val old = EggHelper.getEggTime(item)
                            if (old < itime) {
                                val newt = old + time
                                EggHelper.setEggTime(item, newt)
                                if (newt >= itime)
                                //孵化完成
                                    player.sendMessage("§3物品栏中第§a${i + 1}§3个宝可梦蛋出现了裂纹!")
                                //物品lore进度更新
                                val itemMeta = item.itemMeta!!
                                val v = newt.toDouble() / itime * 100
                                itemMeta.lore =
                                    listOf(
                                        statusInfo(v),
                                        "§3孵化进度:§a${percentageFormat(v)}§3%"
                                    )
                                item.itemMeta = itemMeta
                            }
                            max++
                        }
                    }
                }
            }
        }, time, time)
    }

    fun statusInfo(now: Double): String {
        return when {
            now >= 75.0 -> "里面传来声音。似乎快要孵化了！"
            now >= 50.0 -> "有时会动一下。就快孵化了吧？"
            now >= 25.0 -> "会孵化出什么呢？看来还需要很长时间才能孵化。"
            else -> "这个蛋需要很久才能孵化。"
        }
    }

    fun percentageFormat(value: Double): String {
        return String.format("%.2f", value)
    }

    @EventHandler
    fun interactBlock(event: PlayerInteractEvent) {
        if (event.action.name.contains("RIGHT"))
            event.item?.let {
                interact(event, it)
            }
        /*if (!interact(event, event.player.inventory.itemInMainHand))
            interact(event, event.player.inventory.itemInOffHand)*/
    }

    /**
     * @return 返回事件是否已被取消
     */
    fun interact(event: PlayerEvent, item: ItemStack): Boolean {
        if (event !is Cancellable) throw Exception("event is not cancellable")
        val egg = getEgg(item)
        if (egg == null) return false
        event.isCancelled = true
        //检查是否可以孵化
        val eggTime = EggHelper.getEggTime(item)
        if (eggTime >= CobPasture.INSTANCE.config.getLong("incubation-time")) {
            item.amount = 0
            event.player.uniqueId.getPlayer()!!.party().add(egg)
            event.player.sendMessage("§a孵化成功!")
        }
        return true
    }

    fun getEgg(item: ItemStack?): Pokemon? {
        if (item == null) return null
        if (item.type.name == "CHICKEN_SPAWN_EGG") {
            EggHelper.getPokemonFromEgg(item)?.let {
                return it
            }
        }
        return null
    }

    /*
         * TODO 搁置步数计算逻辑 暂用时间孵化
        @EventHandler
        fun onMove(event: PlayerMoveEvent) {
            if (event.to == null) return
            println(event.to!!.distance(event.from.apply {
                this.y = event.to!!.y
            }))
        }*/
}