package org.figsq.cobpasture.cobpasture.api.playerdata

import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.figsq.cobpasture.cobpasture.api.Pasture
import java.util.*

class PlayerData(
    val uuid: UUID,
) {
    val pastures: MutableSet<Pasture> = mutableSetOf()

    /**
     * 玩家数据的离线玩家实体
     */
    fun getPlayer(): OfflinePlayer {
        return Bukkit.getOfflinePlayer(uuid)
    }

    fun save() {
        DataManager.savePlayerData(this)
    }
}