package org.figsq.cobpasture.cobpasture.api.playerdata

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.scheduler.BukkitTask
import org.figsq.cobpasture.cobpasture.CobPasture
import java.util.UUID

object DataManager : Listener {
    var unifiedPastureDetector: BukkitTask? = null
    val playerDataCache = mutableMapOf<UUID, PlayerData>()
    //可以被替换(用来优化或者实现其他方式存储玩家数据)
    val playerDataManager = PlayerDataManagerImpl

    init {
        /*<-注册监听器->*/
        Bukkit.getPluginManager().registerEvents(this, CobPasture.INSTANCE)
    }

    fun load() {
        //关闭任务
        unifiedPastureDetector?.let {
            try {
                it.cancel()
            } catch (_: Exception) {
            }
        }
        /*<-缓存处理->*/
        playerDataCache.values.forEach { it.save() }
        playerDataCache.clear()
        for (player in Bukkit.getOnlinePlayers())
            playerDataCache.put(player.uniqueId, playerDataManager.getPlayerData(player.uniqueId))

        //重启任务 TODO
    }

    @EventHandler
    fun join(event: PlayerJoinEvent) {
        event.player.uniqueId.let {
            playerDataCache.put(it, playerDataManager.getPlayerData(it))
        }
    }

    @EventHandler
    fun quit(event: PlayerQuitEvent) {
        event.player.uniqueId.let {
            playerDataCache.remove(it)?.save()
        }
    }

    @EventHandler
    fun save(event: WorldSaveEvent) {
        if (Bukkit.getWorlds()[0] != event.world) return
        playerDataCache.values.forEach { it.save() }
        //TODO 或许以后可以搞个提示~
    }
}