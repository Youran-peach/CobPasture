package org.figsq.cobpasture.cobpasture.api.events

import com.cobblemon.mod.common.pokemon.Pokemon
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.bukkit.inventory.ItemStack
import org.figsq.cobpasture.cobpasture.api.Pasture

class TakeItEggEvent(
    val pasture: Pasture,
    val pokemon: Pokemon,
    val eggItem: ItemStack,
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

    override fun setCancelled(p0: Boolean) {
        cancel = p0
    }
}