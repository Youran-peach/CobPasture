package org.figsq.cobpasture.cobpasturelimit

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.figsq.cobpasture.cobpasture.api.events.PastureMakeEggEvent
import org.figsq.cobpasture.cobpasture.api.events.SelectPokemonEvent
import org.figsq.cobpasture.cobpasturelimit.config.MakeEggLimit
import org.figsq.cobpasture.cobpasturelimit.config.SelectLimit

object ListenerBase : Listener {
    @EventHandler
    fun selectPokemon(event: SelectPokemonEvent) {
        if (SelectLimit.isLimit(event.player, event.pokemon)) {
            event.isCancelled = true
            event.player.sendMessage(SelectLimit.tips)
        }
    }

    @EventHandler
    fun makeEgg(event: PastureMakeEggEvent) {
        if (event.egg == null) return

        //是否是限制的
        if (MakeEggLimit.isLimit(event.egg!!)) {
            event.isCancelled = true
            return
        }


        //即刻初始训练家标签
        val owner = event.pasture.ownerUUID ?: return
        if (CobPastureLimit.INSTANCE.config.getBoolean("make-egg-limit.instant-tag-original-trainer"))
            event.egg!!.setOriginalTrainer(owner)
    }
}