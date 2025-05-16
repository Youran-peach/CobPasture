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

    private val inventory = Bukkit.createInventory(this, 54, "Pasture List GUI").apply {
        val itemStack = ItemStack(Material.getMaterial("COBBLEMON_PASTURE")!!)
        val itemMeta = itemStack.itemMeta!!
        for ((index, pasture) in pastures.withIndex()) {
            itemMeta.setDisplayName("Pasture<-${index}->")
            itemStack.itemMeta = itemMeta
            this.addItem(itemStack)
        }
    }

    init {
        onClick { e ->
            e.isCancelled = true
            if (e.clickedInventory is PlayerInventory) return@onClick
            if (e.currentItem == null) return@onClick
            val displayName = e.currentItem?.itemMeta?.displayName ?: return@onClick
            if (!(displayName.contains("Pasture"))) return@onClick
            val index = displayName.substring(9).let {
                it.substring(0, it.length - 2)
            }.toInt()
            e.whoClicked.openInventory(PastureGui(this.pastures[index], this.inventory).inventory)
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }
}