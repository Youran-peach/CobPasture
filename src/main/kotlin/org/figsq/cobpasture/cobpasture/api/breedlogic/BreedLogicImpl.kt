package org.figsq.cobpasture.cobpasture.api.breedlogic

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import org.figsq.cobpasture.cobpasture.api.breedlogic.SpeciesData.DITTO
import org.figsq.cobpasture.cobpasture.api.breedlogic.SpeciesData.NIDORAN_F
import org.figsq.cobpasture.cobpasture.api.breedlogic.SpeciesData.NIDORAN_M
import kotlin.random.Random

object BreedLogicImpl : BreedLogic {
    override fun canBreed(parent1: Pokemon, parent2: Pokemon): Boolean {
        if (parent1 === parent2) return false
        val isDitto1 = parent1.species == DITTO
        val isDitto2 = parent2.species == DITTO
        if (!isDitto1 && !isDitto2) {
            //都不是百变怪的情况下
            if (parent1.gender == parent2.gender) return false
            //非无性且不同性情况下
            if (parent1.gender != Gender.GENDERLESS && parent2.gender != Gender.GENDERLESS) {
                val groups = parent1.species.eggGroups.intersect(parent2.species.eggGroups)
                if (groups.isEmpty()) return false
                return groups.first() != EggGroup.UNDISCOVERED
            }
            return false
        }
        //都是百变怪的情况下
        if (isDitto1 && isDitto2) return false
        //判断非百变怪那只精灵的蛋组出来
        val groups = (if (isDitto1) parent2.species.eggGroups else parent1.species.eggGroups) as Set<EggGroup>
        if (groups.isEmpty()) return false
        return groups.first() != EggGroup.UNDISCOVERED
    }

    override fun makeEgg(
        parent1: Pokemon,
        parent2: Pokemon,
    ): Pokemon? {
        if (!canBreed(parent1, parent2)) return null //不可产出返回空
        //选择继承后的宝可梦物种
        val egg = selectSpecies(parent1, parent2).create(level = 1)
        BreedLogicManager.geneticHandlers.forEach { it.handle(parent1, parent2, egg) }
        return egg
    }

    private fun selectSpecies(parent1: Pokemon, parent2: Pokemon): Species {
        val secondary = if (
            parent1.species == DITTO && (parent2.species != DITTO || parent2.gender == Gender.MALE)
        ) parent2 to parent1 else parent1 to parent2

        val lowestForm = lowestForm(secondary.first.form)
        //规则 若生蛋的母方是尼多兰或者亲代是尼多朗或其进化形和百变怪*，子代由性别*决定是尼多兰还是尼多朗
        if (lowestForm.species.name.startsWith("nidoran",false)) {
            return if (Random.nextBoolean()) NIDORAN_F else NIDORAN_M
        }
        //规则 若生蛋的母方是甜甜萤或者亲代是电萤虫和百变怪*，子代由性别*决定是电萤虫还是甜甜萤。
        if (lowestForm.species.name in setOf("Illumise", "Volbeat"))
            return PokemonSpecies.getByName(if (Random.nextBoolean()) "illumise" else "volbeat")!!
        //规则 玛纳霏只能与百变怪生蛋，子代必定为霏欧纳。
        if (secondary.first.species.name == "Manaphy") return PokemonSpecies.getByName("phione")!!
        //规则 月月熊（赫月）和百变怪生蛋，子代为熊宝宝。
        if (secondary.first.species.name == "Ursaluna" && secondary.first.form.name == "Bloodmoon") return PokemonSpecies.getByName(
            "teddiursa"
        )!!
        return BreedLogicManager.incenseHandler.handle(parent1, parent2) ?: lowestForm(secondary.first.form).species
    }

    /**
     * @return 宝可梦进化链最低的形态
     */
    fun lowestForm(form: FormData): FormData {
        return form.species.preEvolution?.let { lowestForm(it.form) } ?: form
    }
}