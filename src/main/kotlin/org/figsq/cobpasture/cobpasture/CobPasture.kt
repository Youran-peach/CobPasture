package org.figsq.cobpasture.cobpasture

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.figsq.cobpasture.cobpasture.api.breedlogic.BreedLogicManager
import org.figsq.cobpasture.cobpasture.api.playerdata.DataManager

class CobPasture : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: CobPasture
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        this.reloadConfig()
        getCommand("cobpasture")?.let {
            it.setExecutor(CommandBase)
            it.setTabCompleter(CommandBase)
        }

        /*<-  ->*/
        val console = Bukkit.getConsoleSender()
        /*
        //付费下载的时候构建构建这个的
        console.sendMessage("§a哦~插件启动了奥~(哦？你付费买的，为什么不自己构建一个呢，代码开源的~(其实我不想知道为什么))你的钱钱呢~我就勉为其难的收下了呢~")
        * */
        console.sendMessage("§a哦~插件启动了奥~")
    }

    override fun reloadConfig() {
        this.saveDefaultConfig()
        super.reloadConfig()
        BreedLogicManager.load()
        DataManager.load()
    }

    override fun onDisable() {
        super.onDisable()
    }
}