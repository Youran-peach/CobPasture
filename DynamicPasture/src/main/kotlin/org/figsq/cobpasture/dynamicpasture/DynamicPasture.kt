package org.figsq.cobpasture.dynamicpasture

import org.bukkit.plugin.java.JavaPlugin

class DynamicPasture : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: DynamicPasture
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        this.reloadConfig()
    }

    override fun reloadConfig() {
        this.saveDefaultConfig()
        super.reloadConfig()
    }
}