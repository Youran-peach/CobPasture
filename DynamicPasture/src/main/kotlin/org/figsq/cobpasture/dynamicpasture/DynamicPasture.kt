package org.figsq.cobpasture.dynamicpasture

import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.figsq.cobpasture.cobpasture.api.Pasture
import org.figsq.cobpasture.cobpasture.api.events.PlayerDataGetEvent

class DynamicPasture : JavaPlugin(), Listener {
    companion object {
        lateinit var INSTANCE: DynamicPasture
    }

    override fun onLoad() {
        INSTANCE = this
    }

    override fun onEnable() {
        this.reloadConfig()

        getCommand("dynamicpasture")?.setExecutor(this)
        server.pluginManager.registerEvents(this, this)
    }

    override fun reloadConfig() {
        this.saveDefaultConfig()
        super.reloadConfig()

        PerEntry.load()
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String?>?): Boolean {
        if (!(sender.isOp)) {
            sender.sendMessage("§c你没有权限!")
            return false
        }
        this.reloadConfig()
        sender.sendMessage("§a重载成功!")
        return true
    }

    @EventHandler
    fun onPlayerDataGet(event: PlayerDataGetEvent) {
        val playerData = event.playerData
        val player = playerData.getPlayer()
        if (player != null) {
            //玩家纯在的情况下才会进行动态修改牧场
            val max = PerEntry.getPastureQuantity(player)
            val size = playerData.pastures.size
            if (size == max) return

            if (size < max) {
                //小于
                var i = max - size
                while (i > 0) {
                    playerData.pastures.add(Pasture(player.uniqueId))
                    i--
                }
                return
            }
            var i = size - max
            val iterator = playerData.pastures.iterator()
            val party = player.uniqueId.getPlayer()!!.party()
            while (i > 0) {
                if (iterator.hasNext()) {
                    val next = iterator.next()
                    iterator.remove()
                    next.parent1?.let {
                        party.add(it)
                    }
                    next.parent2?.let {
                        party.add(it)
                    }
                }
                i--
            }
            player.sendMessage(config.getString("check-the-return-prompt"))
            //大于
            return
        }
    }
}