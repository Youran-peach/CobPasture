package org.figsq.cobpasture.cobpasture

import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabExecutor
import org.bukkit.entity.Player
import org.figsq.cobpasture.cobpasture.api.Pasture
import org.figsq.cobpasture.cobpasture.api.breedlogic.BreedLogicManager
import org.figsq.cobpasture.cobpasture.api.playerdata.DataManager
import org.figsq.cobpasture.cobpasture.gui.PastureListGui

object CommandBase : TabExecutor {
    val subCmdList = listOf(
        "help", "reload",
        "makeegg",
        "open",
        "add"
    )
    val helpMsg = arrayOf(
        "§a帮助信息",
    )

    override fun onTabComplete(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>,
    ): List<String>? {
        return when (args.size) {
            0 -> subCmdList
            1 -> subCmdList.filter { it.startsWith(args[0]) }
            2 -> when (args[0].lowercase()) {
                "add" -> {
                    return Bukkit.getOnlinePlayers().map { it.name }
                }

                else -> {
                    null
                }
            }

            else -> null
        }
    }

    override fun onCommand(
        sender: CommandSender,
        cmd: Command,
        label: String,
        args: Array<out String>,
    ): Boolean {
        val subCmd = args.getOrNull(0)
        if (subCmdList.contains(subCmd) && subCmd != "help") {
            if (!(sender.hasPermission("cobpasture.cmd.${subCmd}"))) {
                sender.sendMessage("§c你没有权限使用这个命令")
                return false
            }

            return when (subCmd) {
                "reload" -> {
                    CobPasture.INSTANCE.reloadConfig()
                    sender.sendMessage("§a重载配置完成")
                    true
                }

                "makeegg" -> {
                    sender as? Player ?: run {
                        sender.sendMessage("§c这个命令只能在游戏中使用")
                        return false
                    }

                    val parent1Slot = args.getOrNull(1)?.toIntOrNull()?.let {
                        if (it in 1..6) {
                            sender.sendMessage("§c参数错误(parent1Slot) 超出1..6")
                            return false
                        }
                        it
                    } ?: run {
                        sender.sendMessage("§c参数错误(parent1Slot)")
                        return false
                    }
                    val parent2Slot = args.getOrNull(2)?.toIntOrNull()?.let {
                        if (it in 1..6) {
                            sender.sendMessage("§c参数错误(parent1Slot) 超出1..6")
                            return false
                        }
                        it
                    } ?: run {
                        sender.sendMessage("§c参数错误(parent2Slot)")
                        return false
                    }

                    val party = sender.uniqueId.getPlayer()!!.party()
                    val parent1 = party.get(parent1Slot - 1) ?: run {
                        sender.sendMessage("§c${parent1Slot}为空槽!")
                        return false
                    }
                    val parent2 = party.get(parent2Slot - 1) ?: run {
                        sender.sendMessage("§c${parent2Slot}为空槽!")
                        return false
                    }
                    val egg = BreedLogicManager.breedLogic.makeEggPokemon(parent1, parent2)
                    egg?.let {
                        sender.sendMessage("孵化过程跳过了因为没有写!")
                        party.add(it)
                    } ?: sender.sendMessage("§c无法产出!")
                    true
                }

                "open" -> {
                    if (sender !is Player) {
                        sender.sendMessage("§c该指令只能由玩家执行!")
                        return false
                    }

                    val playerData = DataManager.getPlayerData(sender.uniqueId)
                    if (playerData.pastures.isEmpty()) {
                        sender.sendMessage("§c你的牧场数量为0")
                        return false
                    }
                    val gui = PastureListGui(playerData.pastures.toList())
                    sender.openInventory(gui.inventory)
                    true
                }

                /**
                 * 增加一个牧场
                 */
                "add" -> {
                    val player = args.getOrNull(1)?.let { Bukkit.getPlayer(it) } ?: run {
                        sender.sendMessage("§c参数错误(玩家名) 不存在或不在线!")
                        return false
                    }
                    val playerData = DataManager.getPlayerData(player.uniqueId)
                    val max = CobPasture.INSTANCE.config.getInt("player-max-pasture")
                    if (playerData.pastures.size < max) {
                        playerData.pastures.add(Pasture(playerData.uuid))
                        playerData.save()
                        sender.sendMessage("§a添加牧场成功")
                        return true
                    }
                    sender.sendMessage("§c你的牧场数量已经达到上限")
                    false
                }

                else -> false
            }
        }
        sender.sendMessage(*helpMsg)
        return false
    }
}