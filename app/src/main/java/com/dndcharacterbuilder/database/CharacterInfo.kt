package com.dndcharacterbuilder.database

import android.util.Log
import androidx.room.DatabaseView
import com.dndcharacterbuilder.ui.utils.SkillToAbility
import com.dndcharacterbuilder.ui.utils.Skills
import com.dndcharacterbuilder.ui.utils.getModifier

@DatabaseView(
    """
    select c.id as characterId, c.name, c.level,
        c.strength, c.dexterity, c.constitution, c.intelligence, c.wisdom, c.charisma,
        r.name as race, k.name as cclass
    from character as c
    inner join klass as k on k.id = c.cclass
    inner join race as r on r.id = c.race
    """
)
data class CharacterInfo(
    val characterId: Int,
    val name: String,
    val level: Int,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val race: String,
    val cclass: String
){
    fun getSkillModifier(skill: Skills): Int {
        return if (SkillToAbility.strSkills.contains(skill))
            getModifier(strength)
        else if (SkillToAbility.dexSkills.contains(skill))
            getModifier(dexterity)
        else if (SkillToAbility.conSkills.contains(skill))
            getModifier(constitution)
        else if (SkillToAbility.intSkills.contains(skill))
            getModifier(intelligence)
        else if (SkillToAbility.wisSkills.contains(skill))
            getModifier(wisdom)
        else if (SkillToAbility.chaSkills.contains(skill))
            getModifier(charisma)
        else {
            Log.d("ERR", "Couldn't find ability for skill ${skill.name}")
            0
        }
    }

}
