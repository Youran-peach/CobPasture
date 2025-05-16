package org.figsq.cobpasture.cobpasture.api.events

import com.cobblemon.mod.common.pokemon.Pokemon
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.figsq.cobpasture.cobpasture.api.Pasture

/**
 * 牧场产蛋时触发
 * 修改 egg 可以修改产出的结果
 * 可以被取消，取消后的效果则是直接跳过本次产出
 */
class PastureMakeEggEvent(
    val pasture: Pasture,
    var egg: Pokemon?,
) : Event(), Cancellable {
    companion object {
        private var handlerList: HandlerList = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlerList
        }
    }

    var cancel = false

    override fun getHandlers(): HandlerList {
        return handlerList
    }

    override fun isCancelled(): Boolean {
        return cancel
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancel = cancel
    }
}