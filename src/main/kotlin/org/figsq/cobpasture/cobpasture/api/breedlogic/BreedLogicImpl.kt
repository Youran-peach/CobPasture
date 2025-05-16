package org.figsq.cobpasture.cobpasture.api.breedlogic

import com.cobblemon.mod.common.api.pokemon.PokemonSpecies
import com.cobblemon.mod.common.api.pokemon.egg.EggGroup
import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.pokemon.Gender
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.pokemon.Species
import org.figsq.cobpasture.cobpasture.CobPasture
import kotlin.random.Random

object BreedLogicImpl : BreedLogic {
    override fun canBreed(parent1: Pokemon, parent2: Pokemon): Boolean {
        if (parent1 === parent2) return false
        val isDitto1 = parent1.species.name == "ditto"
        val isDitto2 = parent2.species.name == "ditto"
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
        return groups.find { it == EggGroup.UNDISCOVERED } != null
    }

    override fun makeEgg(
        parent1: Pokemon,
        parent2: Pokemon,
    ): Pokemon? {
        if (!canBreed(parent1, parent2)) return null //不可产出返回空
        //选择继承后的宝可梦物种
        val egg = selectSpecies(parent1, parent2).create(level = 1)
        geneticData(parent1, parent2, egg)
        return egg
    }

    private fun geneticData(parent1: Pokemon, parent2: Pokemon, egg: Pokemon) {
        //TODO 遗传数据为实现
        CobPasture.INSTANCE.logger.warning("遗传数据为实现!(只是提示不用在意(并没有关闭提示的方法~))")
    }

    private fun selectSpecies(parent1: Pokemon, parent2: Pokemon): Species {
        val secondary = if (
            parent1.species.name != "ditto" && (parent2.species.name != "ditto" || parent2.gender == Gender.MALE)
        ) parent1 to parent2 else parent2 to parent1

        val lowestForm = lowestForm(secondary.first.form)
        //规则 若生蛋的母方是尼多兰或者亲代是尼多朗或其进化形和百变怪*，子代由性别*决定是尼多兰还是尼多朗
        if (lowestForm.species.name.startsWith("Nidoran"))
            return PokemonSpecies.getByName("nidoran${if (Random.nextBoolean()) "m" else "f"}")!!
        //规则 若生蛋的母方是甜甜萤或者亲代是电萤虫和百变怪*，子代由性别*决定是电萤虫还是甜甜萤。
        if (lowestForm.species.name in setOf("Illumise", "Volbeat"))
            return PokemonSpecies.getByName(if (Random.nextBoolean()) "illumise" else "volbeat")!!
        //规则 玛纳霏只能与百变怪生蛋，子代必定为霏欧纳。
        if (secondary.first.species.name == "Manaphy") return PokemonSpecies.getByName("phione")!!
        //规则 月月熊（赫月）和百变怪生蛋，子代为熊宝宝。
        if (secondary.first.species.name == "Ursaluna" && secondary.first.form.name == "Bloodmoon") return PokemonSpecies.getByName(
            "teddiursa"
        )!!
        //规则 熏香 TODO 没写 朱紫 的 无视熏香
        CobPasture.INSTANCE.logger.warning("熏香逻辑未实现!(只是提示不用在意(并没有关闭提示的方法~))")

        return lowestForm(secondary.first.form).species
    }

    /**
     * @return 宝可梦进化链最低的形态
     */
    private fun lowestForm(form: FormData): FormData {
        return form.species.preEvolution?.let { lowestForm(it.form) } ?: form
    }
}