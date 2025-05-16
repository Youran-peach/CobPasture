package org.figsq.cobpasture.cobpasture.api.playerdata

import java.util.*

interface PlayerDataManager {
    fun getPlayerData(uuid: UUID): PlayerData
    fun savePlayerData(playerData: PlayerData)
}