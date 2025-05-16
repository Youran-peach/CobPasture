package org.figsq.cobpasture.cobpasture.gui

import com.cobblemon.mod.common.item.PokemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.getPlayer
import com.cobblemon.mod.common.util.party
import com.cobblemon.mod.common.util.pc
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.HumanEntity
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.figsq.cobpasture.cobpasture.CobPasture
import org.figsq.cobpasture.cobpasture.api.EggHelper
import org.figsq.cobpasture.cobpasture.api.Pasture

class PastureGui(
    val pasture: Pasture,
    var previous: Inventory?,
) : ListenerInvHolder(), AcceptOptional<PartySelectGui> {
    companion object {
        private fun leg(pastureGui: PastureGui, pokemon: Pokemon) {
            val player = pastureGui.pasture.ownerUUID!!.getPlayer()!!
            val party = player.party()
            val pc = player.pc()
            party[pokemon.uuid]?.let {
                party.remove(it)
            } ?: run {
                pc[pokemon.uuid]?.let {
                    pc.remove(it)
                }
            }
            pastureGui.pasture.parent2?.let {
                party.add(it)
            }
        }
    }

    enum class OptionalSlot(
        val setSlot: (PastureGui, Pokemon) -> Unit,
    ) {
        PARENT1({ pastureGui, pokemon ->
            leg(pastureGui, pokemon)
            pastureGui.pasture.parent1 = pokemon
            pastureGui.inventory.setItem(10, CraftItemStack.asBukkitCopy(PokemonItem.from(pokemon)).apply {
                val itemMeta = this.itemMeta!!
                itemMeta.setDisplayName("${pokemon.getDisplayName().string}")
                itemMeta.lore = listOf("Parent 1")
            })
        }),
        PARENT2({ pastureGui, pokemon ->
            leg(pastureGui, pokemon)
            pastureGui.pasture.parent2 = pokemon
            pastureGui.inventory.setItem(16, CraftItemStack.asBukkitCopy(PokemonItem.from(pokemon)).apply {
                val itemMeta = this.itemMeta!!
                itemMeta.setDisplayName("${pokemon.getDisplayName().string}")
                itemMeta.lore = listOf("Parent 2")
            })
        });

        fun setSlot(pastureGui: PastureGui, pokemon: Pokemon) {
            setSlot.invoke(pastureGui, pokemon)
        }
    }

    private val inventory = Bukkit.createInventory(this, 27, "Pasture Gui").apply {
        /*
        "aaabbbccc"
        "a#ab$bc%c"
        "aaabbbccc"
        * */
        val a = ItemStack(Material.PINK_STAINED_GLASS_PANE)
        val aMeta = a.itemMeta!!
        aMeta.setDisplayName(" ")
        aMeta.lore = emptyList<String>()
        a.itemMeta = aMeta
        val b = ItemStack(Material.GREEN_STAINED_GLASS_PANE)
        val bMeta = b.itemMeta!!
        bMeta.setDisplayName(" ")
        bMeta.lore = emptyList<String>()
        b.itemMeta = bMeta
        val c = ItemStack(Material.BLUE_STAINED_GLASS_PANE)
        val cMeta = c.itemMeta!!
        cMeta.setDisplayName(" ")
        cMeta.lore = emptyList<String>()
        c.itemMeta = cMeta
        var item = a;
        var step = 0;
        var start = 0
        while (start < 27) {
            this.setItem(start, item)
            start++
            step++
            if (step == 3) {
                step = 0
                item = if (item == a) b else if (item == b) c else a
            }
        }

        this.setItem(10, pasture.parent1?.let {
            CraftItemStack.asBukkitCopy(PokemonItem.from(it)).apply {
                val itemMeta = this.itemMeta!!
                itemMeta.setDisplayName("${it.getDisplayName().string}")
                itemMeta.lore = listOf("Parent 1")
            }
        })
        this.setItem(13, pasture.egg?.let {
            val itemStack = ItemStack(Material.CHICKEN_SPAWN_EGG)
            val itemMeta = itemStack.itemMeta!!
            itemMeta.setDisplayName("蛋")
            itemMeta.lore = listOf("这个蛋需要很久才能孵化。")
            itemStack.itemMeta = itemMeta
            itemStack
        })
        this.setItem(16, pasture.parent2?.let {
            CraftItemStack.asBukkitCopy(PokemonItem.from(it)).apply {
                val itemMeta = this.itemMeta!!
                itemMeta.setDisplayName("${it.getDisplayName().string}")
                itemMeta.lore = listOf("Parent 2")
            }
        })
    }

    init {
        onClose { e ->
            if (previous == null) return@onClose
            Bukkit.getScheduler().runTask(CobPasture.INSTANCE, Runnable {
                e.player.openInventory(previous!!)
            })
        }

        onClick { e ->
            e.isCancelled = true

            if (e.clickedInventory is PlayerInventory) return@onClick
            val slot = e.slot

            //parent1
            val human = e.whoClicked
            if (slot == 10) {
                if (pasture.parent1 == null) {
                    //选择精灵
                    silentOpen(
                        human, PartySelectGui(
                            human.uniqueId.getPlayer()!!.party(), OptionalSlot.PARENT1, this
                        ).inventory
                    )
                    return@onClick
                }
                //移除 收回
                human.uniqueId.getPlayer()!!.party().add(pasture.parent1!!)
                pasture.parent1 = null
                this.inventory.setItem(10, null)
                return@onClick
            }

            //parent2
            if (slot == 16) {
                if (pasture.parent2 == null) {
                    //选择精灵
                    silentOpen(
                        human, PartySelectGui(
                            human.uniqueId.getPlayer()!!.party(), OptionalSlot.PARENT2, this
                        ).inventory
                    )
                    return@onClick
                }
                //移除 收回
                human.uniqueId.getPlayer()!!.party().add(pasture.parent2!!)
                pasture.parent2 = null
                this.inventory.setItem(16, null)
                return@onClick
            }

            //egg
            if (slot == 13) {
                if (pasture.egg == null) return@onClick
                val itemStack = ItemStack(Material.CHICKEN_SPAWN_EGG)
                EggHelper.writePokemonFromEgg(itemStack, pasture.egg!!)
                val itemMeta = itemStack.itemMeta!!
                itemMeta.setDisplayName("Poke Egg")
                itemMeta.lore = listOf("这个蛋需要很久才能孵化。")
                itemStack.itemMeta = itemMeta

                val eyeLocation = human.eyeLocation
                val dropItem = eyeLocation.world!!.dropItem(eyeLocation, itemStack)
                dropItem.pickupDelay = 0
                dropItem.isInvulnerable = true

                pasture.egg = null
                this.inventory.setItem(13, null)
                return@onClick
            }
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    override fun accept(e: PartySelectGui) {
        e.pokemon?.let {
            e.optionalSlot.setSlot(this, it)
        }
    }

    fun silentOpen(human: HumanEntity, inventory: Inventory) {
        this.previous.apply {
            previous = null
            human.openInventory(inventory)
        }.also { this.previous = it }
    }
}