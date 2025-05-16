package org.figsq.cobpasture.cobpasture.api.playerdata

import org.figsq.cobpasture.cobpasture.CobPasture
import org.figsq.cobpasture.cobpasture.api.gsonadapter.GsonHelper
import java.io.File
import java.util.*

object PlayerDataManagerImpl : PlayerDataManager {
    val folder: File by lazy {
        File(CobPasture.INSTANCE.dataFolder, "playerdata")
    }

    override fun getPlayerData(uuid: UUID): PlayerData {
        val file = getPlayerDataFile(uuid)
        if (!(file.exists())) return PlayerData(uuid)
        val reader = file.reader()
        return GsonHelper.GSON.fromJson(reader, PlayerData::class.java)
            .apply {
                reader.close()
            }
    }

    override fun savePlayerData(playerData: PlayerData) {
        val file = getPlayerDataFile(playerData.uuid)
        checkOrCreate(file)
        val writer = file.writer()
        GsonHelper.GSON.toJson(playerData, writer)
        writer.close()
    }

    fun getPlayerDataFile(uuid: UUID): File {
        return File(folder, "$uuid.json")
    }

    fun checkOrCreate(file: File) {
        if (!(file.exists())) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }
    }
}