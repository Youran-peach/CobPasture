package org.figsq.cobpasture.cobpasture.api

import com.cobblemon.mod.common.pokemon.Pokemon
import de.tr7zw.nbtapi.NBT
import org.bukkit.inventory.ItemStack
import org.figsq.cobpasture.cobpasture.api.gsonadapter.GsonHelper

object EggHelper {
    fun getPokemonFromEgg(item: ItemStack): Pokemon? {
        return NBT.get<Pokemon>(item) { nbt ->
            nbt.getString("cobpasture.poke")?.let {
                GsonHelper.GSON.fromJson(it, Pokemon::class.java)
            }
        }
    }

    fun writePokemonFromEgg(item: ItemStack, pokemon: Pokemon) {
        NBT.modify(item) { nbt ->
            nbt.setString("cobpasture.poke", GsonHelper.GSON.toJson(pokemon))
        }
    }

    fun eggHasPokemon(item: ItemStack): Boolean {
        return getPokemonFromEgg(item) != null
    }

    fun getEggStep(): Int {
        TODO("Not implemented")
    }

    fun getEggCycle(): Int {
        TODO("Not implemented")
    }

    fun setEggStep(step: Int) {
        TODO("Not implemented")
    }

    fun setEggCycle(cycle: Int) {
        TODO("Not implemented")
    }

    /**
     * 获取已孵化的时长
     */
    fun getEggTime(item: ItemStack): Long {
        return NBT.get<Long>(item) { nbt ->
            nbt.getLong("cobpasture.time")
        }
    }

    /**
     * 设置已孵化的时长
     */
    fun setEggTime(item: ItemStack, time: Long) {
        NBT.modify(item) { nbt ->
            nbt.setLong("cobpasture.time", time)
        }
    }
}