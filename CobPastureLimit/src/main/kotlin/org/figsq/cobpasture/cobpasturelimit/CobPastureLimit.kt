package org.figsq.cobpasture.cobpasturelimit

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin
import org.figsq.cobpasture.cobpasturelimit.config.MakeEggLimit
import org.figsq.cobpasture.cobpasturelimit.config.SelectLimit

class CobPastureLimit : JavaPlugin() {
    companion object {
        lateinit var INSTANCE: CobPastureLimit
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        this.reloadConfig()

        val pluginManager = Bukkit.getServer().pluginManager
        pluginManager.registerEvents(ListenerBase, this)

        getCommand("cobpasturelimit")?.setExecutor(this)
    }

    override fun reloadConfig() {
        this.saveDefaultConfig()
        super.reloadConfig()
        SelectLimit.load()
        MakeEggLimit.load()
    }


    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String?>?): Boolean {
        if (!(sender.isOp)) {
            sender.sendMessage("§c你没有权限使用这个命令!")
            return false
        }
        this.reloadConfig()
        sender.sendMessage("§a配置已重载!")
        return true
    }
}