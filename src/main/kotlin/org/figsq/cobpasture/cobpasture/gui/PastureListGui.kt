package org.figsq.cobpasture.cobpasture.gui

import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder
import org.bukkit.inventory.Inventory
import org.figsq.cobpasture.cobpasture.api.Pasture

class PastureListGui(
    pastures: Set<Pasture>
) : ListenerInvHolder() {
    override fun getInventory(): Inventory {
        TODO("Not yet implemented")
    }
}