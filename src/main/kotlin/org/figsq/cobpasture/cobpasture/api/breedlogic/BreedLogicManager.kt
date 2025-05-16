package org.figsq.cobpasture.cobpasture.api.breedlogic

object BreedLogicManager {
    //可以被替换的
    var breedLogic: BreedLogic = BreedLogicImpl

    fun load() {
        //TODO 加载
    }
}