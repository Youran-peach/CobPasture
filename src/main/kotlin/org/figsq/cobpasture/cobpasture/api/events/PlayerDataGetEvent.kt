package org.figsq.cobpasture.cobpasture.api.events

import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.figsq.cobpasture.cobpasture.api.playerdata.PlayerData

/**
 * 玩家数据被获取的时候触发，但并不是玩家数据管理器实现内触发，而是DataManager获取的时候触发
 * 方便数据在获取的时候进行修改等操作
 */
class PlayerDataGetEvent(
    val playerData: PlayerData,
) : Event() {
    companion object {
        private var handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    override fun getHandlers(): HandlerList {
        return handlerList
    }
}