package com.dndcharacterbuilder.ui.utils

import android.content.Context
import com.dndcharacterbuilder.R

enum class Skills (private val stringId: Int) {
    ACROBATICS (R.string.acrobatics),
    ANIMAL_HANDLING (R.string.animal_handling),
    ARCANA (R.string.arcana),
    ATHLETICS (R.string.athletics),
    DECEPTION (R.string.deception),
    HISTORY (R.string.history),
    INSIGHT (R.string.insight),
    INTIMIDATION (R.string.intimidation),
    INVESTIGATION (R.string.investigation),
    MEDICINE (R.string.medicine),
    NATURE (R.string.nature),
    PERCEPTION (R.string.perception),
    PERFORMANCE (R.string.performance),
    PERSUASION (R.string.persuasion),
    RELIGION (R.string.religion),
    SLEIGHT_OF_HAND (R.string.sleight_of_hand),
    STEALTH (R.string.stealth),
    SURVIVAL (R.string.survival),
    UNDEFINED (R.string.empty_string);
    companion object{
        fun getSkillFromName(name: String, context: Context): Skills {
            return Skills.values().find { name == context.getString(it.stringId) } ?: UNDEFINED
        }
        fun getNameFromSkill(skill: Skills, context: Context): String {
            return context.getString(skill.stringId)
        }
    }
}
