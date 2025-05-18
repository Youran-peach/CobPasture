package org.figsq.cobpasture.cobpasture.api.playerdata

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.figsq.cobpasture.cobpasture.api.Pasture
import java.util.*

class PlayerData(
    val uuid: UUID,
) {
    val pastures: MutableSet<Pasture> = mutableSetOf()

    /**
     * 玩家数据的玩家 离线玩家会导致权限没法判断所以数据都是在玩家腿粗后就删了，所以不建议在玩家不存在的时候对数据进行操作
     */
    fun getPlayer(): Player? {
        return Bukkit.getPlayer(uuid)
    }

    fun save() {
        DataManager.savePlayerData(this)
    }
}