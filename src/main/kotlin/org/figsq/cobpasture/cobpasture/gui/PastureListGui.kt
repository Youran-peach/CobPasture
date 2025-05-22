package org.figsq.cobpasture.cobpasture.gui

import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.figsq.cobpasture.cobpasture.api.Pasture

class PastureListGui(
    val pastures: List<Pasture>,
) : ListenerInvHolder() {
    private val temp = mutableMapOf<Int, Pasture>()

    private val inventory = Bukkit.createInventory(this, 54, GuiConfig.PASTURE_LIST_GUI_TITLE).apply {
        val itemStack = ItemStack(Material.getMaterial("COBBLEMON_PASTURE")!!)
        val itemMeta = itemStack.itemMeta!!
        for ((index, pasture) in pastures.withIndex()) {
            itemMeta.setDisplayName(
                GuiConfig.PASTURE_LIST_GUI_ELEMENTS_NAME
                    .replace("{pasture_id}", index.toString())
            )
            itemMeta.lore = GuiConfig.PASTURE_LIST_GUI_ELEMENTS_LORE.map {
                it.replace("{pasture_id}", index.toString())
                    .replace("{pasture_tick}", "${pasture.tick}")
                    .replace("{pasture_progress}", pasture.progress())
            }
            itemStack.itemMeta = itemMeta
            this.setItem(index, itemStack)
            temp[index] = pasture
        }
    }

    init {
        onClick { e ->
            e.isCancelled = true
            if (e.clickedInventory is PlayerInventory) return@onClick
            if (e.currentItem == null) return@onClick
            val pasture = temp[e.slot]
            if (pasture == null) return@onClick
            e.whoClicked.openInventory(PastureGui(pasture, this.inventory).inventory)
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}