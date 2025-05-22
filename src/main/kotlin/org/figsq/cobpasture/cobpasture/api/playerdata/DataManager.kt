package org.figsq.cobpasture.cobpasture.api.playerdata

import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.world.WorldSaveEvent
import org.bukkit.scheduler.BukkitTask
import org.figsq.cobpasture.cobpasture.CobPasture
import org.figsq.cobpasture.cobpasture.api.events.PlayerDataGetEvent
import java.util.*

object DataManager : Listener, PlayerDataManager {
    private var unifiedPastureDetector: BukkitTask? = null
    val playerDataCache = mutableMapOf<UUID, PlayerData>()

    //可以被替换(用来优化或者实现其他方式存储玩家数据)
    /*不要直接调用这个来获取玩家数据(会导致不读缓存!)*/
    private var playerDataManager: PlayerDataManager = PlayerDataManagerImpl

    /**
     * 替换实现
     */
    fun setPlayerDataManagerImpl(playerDataManager: PlayerDataManager) {
        this.playerDataManager = playerDataManager
    }

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
        for (player in Bukkit.getOnlinePlayers()) {
            /*在线玩家的缓存重加载*/
            this.getPlayerData(player.uniqueId)
        }
        /*重启*/
        val instance = CobPasture.INSTANCE
        val config = instance.config
        val time = config.getLong("pasture-check-time")
        val makeEggTime = config.getLong("pasture-make-egg-time")
        unifiedPastureDetector = Bukkit.getScheduler().runTaskTimer(
            instance, Runnable {
                for (pasture in playerDataCache.values.flatMap { it.pastures }) {
                    if (pasture.needCheck()) {
                        pasture.tick += time
                        if (pasture.tick < makeEggTime) continue
                        pasture.makeEgg()
                    }
                }
            }, time, time
        )
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

    override fun getPlayerData(uuid: UUID): PlayerData {
        return playerDataCache.getOrPut(uuid) {
            playerDataManager.getPlayerData(uuid)
        }.apply {
            Bukkit.getServer().pluginManager.callEvent(PlayerDataGetEvent(this))
        }
    }

    override fun savePlayerData(playerData: PlayerData) {
        playerDataManager.savePlayerData(playerData)
    }
}