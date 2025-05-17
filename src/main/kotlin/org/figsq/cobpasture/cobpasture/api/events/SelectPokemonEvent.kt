package org.figsq.cobpasture.cobpasture.api.events

import com.cobblemon.mod.common.pokemon.Pokemon
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.HandlerList
import org.figsq.cobpasture.cobpasture.gui.PartySelectGui

class SelectPokemonEvent(
    val player: Player,
    val pokemon: Pokemon,
    /**
     * 这里面存有目标牧场数据，和目标牧场存入的位置
     */
    val partySelectGui: PartySelectGui,
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