package org.figsq.cobpasture.dynamicpasture

import org.bukkit.permissions.Permissible

class PerEntry(
    val permission: String,
    var quantity: Int,
) {
    fun hasPermission(permissible: Permissible): Boolean {
        return permissible.hasPermission(permission)
    }


    companion object {
        lateinit var perEntryList: MutableList<PerEntry>
        fun load() {
            val config = DynamicPasture.INSTANCE.config
            perEntryList = mutableListOf()
            for (map in config.getMapList("permissions")) {
                perEntryList.add(
                    PerEntry(
                        map["permission"].toString(),
                        (map["quantity"] as Number).toInt()
                    )
                )
            }
            perEntryList.sortByDescending(PerEntry::quantity)
        }

        fun getPastureQuantity(permissible: Permissible): Int {
            for (entry in perEntryList) if (entry.hasPermission(permissible)) return entry.quantity
            return 0
        }
    }
}