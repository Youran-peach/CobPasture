package org.figsq.cobpasture.cobpasture.api.playerdata

import java.util.UUID

interface PlayerDataManager {
    fun getPlayerData(uuid: UUID): PlayerData
    fun savePlayerData(playerData: PlayerData)
}