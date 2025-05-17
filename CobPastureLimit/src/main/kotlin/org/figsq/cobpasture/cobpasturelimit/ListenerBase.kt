package org.figsq.cobpasture.cobpasturelimit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.figsq.cobpasture.cobpasture.api.events.PastureMakeEggEvent
import org.figsq.cobpasture.cobpasture.api.events.SelectPokemonEvent

object ListenerBase : Listener {
    @EventHandler
    fun selectPokemon(event: SelectPokemonEvent) {
        TODO()
    }

    @EventHandler
    fun makeEgg(event: PastureMakeEggEvent) {
        TODO()
    }
}