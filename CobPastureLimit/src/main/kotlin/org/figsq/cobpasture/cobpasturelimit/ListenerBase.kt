package org.figsq.cobpasture.cobpasturelimit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.figsq.cobpasture.cobpasture.api.events.PastureMakeEggEvent
import org.figsq.cobpasture.cobpasture.api.events.SelectPokemonEvent
import org.figsq.cobpasture.cobpasturelimit.config.SelectLimit

object ListenerBase : Listener {
    @EventHandler
    fun selectPokemon(event: SelectPokemonEvent) {
        if (SelectLimit.isLimit(event.pokemon)) {
            event.isCancelled = true
            event.player.sendMessage(SelectLimit.tips)
        }
    }

    /*@EventHandler
    fun makeEgg(event: PastureMakeEggEvent) {
        TODO()
    }*/
}