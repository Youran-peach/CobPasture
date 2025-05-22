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
import org.figsq.cobpasture.cobpasture.api.events.SelectPokemonEvent

class PartySelectGui(
    party: PlayerPartyStore,
    val optionalSlot: PastureGui.OptionalSlot,
    var previous: AcceptOptional<PartySelectGui>?,
) : ListenerInvHolder(), Optional<Pair<Pokemon?, PastureGui.OptionalSlot>> {
    var pokemon: Pokemon? = null
    val player = Bukkit.getPlayer(party.playerUUID)!!
    val temp: Map<Int, Pokemon>

    private val inventory = Bukkit.createInventory(this, 9, GuiConfig.PARTY_SELECT_GUI_TITLE)

    init {
        temp = party.mapIndexed { index, pokemon ->
            inventory.setItem(index, CraftItemStack.asBukkitCopy(PokemonItem.from(pokemon)).apply {
                val meta = this.itemMeta!!
                meta.setDisplayName(
                    GuiConfig.PARTY_SELECT_GUI_ELEMENTS_NAME
                        .replace("{pokemon_name}", pokemon.getDisplayName().string)
                )
                meta.lore = GuiConfig.PARTY_SELECT_GUI_ELEMENTS_LORE.map {
                    it.replace("{pokemon_name}", pokemon.getDisplayName().string)
                }
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
                val event = SelectPokemonEvent(
                    player, it, this
                )
                Bukkit.getPluginManager().callEvent(event)
                if (event.isCancelled) return@onClick

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