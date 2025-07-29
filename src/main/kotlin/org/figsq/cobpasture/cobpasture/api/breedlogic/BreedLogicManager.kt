package org.figsq.cobpasture.cobpasture.api.breedlogic

import org.figsq.cobpasture.cobpasture.api.breedlogic.genetic.IGeneticHandler
import org.figsq.cobpasture.cobpasture.api.breedlogic.genetic.IVGenetic
import org.figsq.cobpasture.cobpasture.api.breedlogic.genetic.MoveGenetic
import org.figsq.cobpasture.cobpasture.api.breedlogic.genetic.NatureGenetic
import org.figsq.cobpasture.cobpasture.api.breedlogic.incense.IIncenseHandler
import org.figsq.cobpasture.cobpasture.api.breedlogic.incense.IncenseHandlerImpl

object BreedLogicManager {
    //可以被替换的
    var breedLogic: BreedLogic = BreedLogicImpl
    var incenseHandler: IIncenseHandler = IncenseHandlerImpl
    /**
     * 所有遗传处理
     */
    var geneticHandlers: MutableList<IGeneticHandler> = mutableListOf(
        IVGenetic, NatureGenetic, MoveGenetic
    )

    fun load() {
        //TODO 加载
    }
}