package org.figsq.cobpasture.cobpasture.gui

import com.cobblemon.mod.common.api.storage.party.PlayerPartyStore
import com.cobblemon.mod.common.item.PokemonItem
import com.cobblemon.mod.common.pokemon.Pokemon
import me.fullidle.ficore.ficore.common.api.ineventory.ListenerInvHolder
import me.fullidle.ficore.ficore.common.bukkit.inventory.CraftItemStack
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.PlayerInventory
import org.figsq.cobpasture.cobpasture.CobPasture

class PartySelectGui(
    party: PlayerPartyStore,
    val optionalSlot: PastureGui.OptionalSlot,
    var previous: AcceptOptional<PartySelectGui>?,
) : ListenerInvHolder(), Optional<Pair<Pokemon?, PastureGui.OptionalSlot>> {
    var pokemon: Pokemon? = null
    val player = Bukkit.getPlayer(party.playerUUID)!!
    val temp: Map<Int, Pokemon>

    private val inventory = Bukkit.createInventory(this, 9, "Party Select GUI")

    init {
        temp = party.mapIndexed { index, pokemon ->
            inventory.setItem(index, CraftItemStack.asBukkitCopy(PokemonItem.from(pokemon)).apply {
                val meta = this.itemMeta!!
                meta.setDisplayName(pokemon.getDisplayName().string)
                meta.lore = listOf("点击选择")
                this.itemMeta = meta
            })
            index to pokemon
        }.toMap()

        onClick { e ->
            e.isCancelled = true
            if (e.clickedInventory is PlayerInventory) return@onClick
            val human = e.whoClicked
            val slot = e.slot
            temp[slot]?.let {
                pokemon = it
                human.closeInventory()
            }
        }

        onClose {
            callback()
        }
    }

    override fun getInventory(): Inventory {
        return inventory
    }

    override fun get(): Pair<Pokemon?, PastureGui.OptionalSlot> {
        return pokemon to optionalSlot
    }

    fun callback() {
        this.previous?.let {
            it.accept(this)
            if (it !is PastureGui) return
            Bukkit.getScheduler().runTask(CobPasture.INSTANCE, Runnable {
                player.openInventory(it.inventory)
            })
        }
    }
}