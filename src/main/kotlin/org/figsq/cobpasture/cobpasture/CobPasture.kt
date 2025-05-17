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

    val LOGO = arrayOf(
        "",
        "§3  _____     __   §7___           §8__              ",
        "§3 / ___/__  / /  §7/ _ \\___ ____ §8/ /___ _________ ",
        "§3/ /__/ _ \\/ _ \\§7/ ___/ _ `(_-<§8/ __/ // / __/ -_)",
        "§3\\___/\\___/_.__§7/_/   \\_,_/___/§8\\__/\\_,_/_/  \\__/ ",
        ""
    )

    override fun onEnable() {
        this.reloadConfig()
        getCommand("cobpasture")?.let {
            it.setExecutor(CommandBase)
            it.setTabCompleter(CommandBase)
        }
        server.pluginManager.registerEvents(ListenerBase,this)

        /*<-  ->*/
        val console = Bukkit.getConsoleSender()
        console.sendMessage(*LOGO)

        //付费下载的时候构建构建这个的 不想要有自己删了构建去
        console.sendMessage("§a(哦？你付费买的，为什么不自己构建一个呢，代码开源的~(其实我不想知道为什么))你的钱钱呢~我就勉为其难的收下了呢~")
        console.sendMessage("§a哦~插件启动了奥~")
    }

    override fun reloadConfig() {
        this.saveDefaultConfig()
        super.reloadConfig()
        BreedLogicManager.load()
        DataManager.load()
        ListenerBase.load()
    }

    override fun onDisable() {
        //保存数据
        for (data in DataManager.playerDataCache.values) data.save()
    }
}