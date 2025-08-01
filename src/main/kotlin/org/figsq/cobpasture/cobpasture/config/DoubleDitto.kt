package org.figsq.cobpasture.cobpasture.config

import org.figsq.cobpasture.cobpasture.CobPasture

object DoubleDitto {
    var enable = false

    fun load(){
        val section = CobPasture.INSTANCE.config.getConfigurationSection("double-ditto-breed") ?: return
        enable = section.getBoolean("enable", false)
    }
}
