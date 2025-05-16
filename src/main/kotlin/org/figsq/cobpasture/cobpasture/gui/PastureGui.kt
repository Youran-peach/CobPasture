package org.figsq.cobpasture.cobpasture.gui

import com.cobblemon.mod.common.item.PokemonItem
import de.tr7zw.nbtapi.NBT
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.figsq.cobpasture.cobpasture.CobPasture
import org.figsq.cobpasture.cobpasture.api.Pasture
import org.figsq.cobpasture.cobpasture.api.gsonadapter.GsonHelper

class PastureGui(
    val pasture: Pasture,
    val previous: Inventory?,
) : ListenerInvHolder() {
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
            if (step == 2) {
                step = 0
                item = if (item == b) c else b
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
            itemMeta.lore = listOf("拿在手上走一段路程后孵化")
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
                e.player.openInventory(previous)
            })
        }

        onClick { e ->
            e.isCancelled = true

            if (e.clickedInventory is PlayerInventory) return@onClick
            val slot = e.slot

            //parent1
            if (slot == 10) {
                //TODO 父母宝可梦设置没写
            }

            //parent2
            if (slot == 16) {

            }

            //egg
            if (slot == 13) {
                if (pasture.egg == null) return@onClick
                val itemStack = ItemStack(Material.CHICKEN_SPAWN_EGG)
                NBT.modify(itemStack) { nbt ->
                    nbt.setString("cobpasture", GsonHelper.GSON.toJson(pasture.egg!!))
                }

                val eyeLocation = e.whoClicked.eyeLocation
                val dropItem = eyeLocation.world!!.dropItem(eyeLocation, itemStack)
                dropItem.pickupDelay = 0
                dropItem.isInvulnerable = true

                pasture.egg = null
                return@onClick
            }
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}