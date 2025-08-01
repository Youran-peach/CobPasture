package org.figsq.cobpasture.cobpasture.api.breedlogic.incense

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species

interface IIncenseHandler {
    /**
     * 处理熏香结果，在计算的时候，如果没有熏香或者不需要进行熏香处理，则返回空
     * 为空时继续走默认，不为空则直接是最终结果
     * @return 最终物种 (可以为空) 在 [org.figsq.cobpasture.cobpasture.api.breedlogic.BreedLogicImpl.selectSpecies]
     * 如果该处理返回为空啧不会进行熏香处理而是直接跳过换成物种进化链第一个
     */
    fun handle(parent1: Pokemon, parent2: Pokemon): Species?
}