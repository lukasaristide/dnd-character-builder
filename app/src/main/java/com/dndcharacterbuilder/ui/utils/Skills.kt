package com.dndcharacterbuilder.ui.utils

import android.content.Context
import com.dndcharacterbuilder.R

enum class Skills {
    ACROBATICS,
    ANIMAL_HANDLING,
    ARCANA,
    ATHLETICS,
    DECEPTION,
    HISTORY,
    INSIGHT,
    INTIMIDATION,
    INVESTIGATION,
    MEDICINE,
    NATURE,
    PERCEPTION,
    PERFORMANCE,
    PERSUASION,
    RELIGION,
    SLEIGHT_OF_HAND,
    STEALTH,
    SURVIVAL,
    UNDEFINED;
    companion object{
        fun getSkillFromName(name: String, context: Context): Skills {
            return when(name){
                context.getString(R.string.acrobatics) -> ACROBATICS
                context.getString(R.string.animal_handling) -> ANIMAL_HANDLING
                context.getString(R.string.arcana) -> ARCANA
                context.getString(R.string.athletics) -> ATHLETICS
                context.getString(R.string.deception) -> DECEPTION
                context.getString(R.string.history) -> HISTORY
                context.getString(R.string.insight) -> INSIGHT
                context.getString(R.string.intimidation) -> INTIMIDATION
                context.getString(R.string.investigation) -> INVESTIGATION
                context.getString(R.string.medicine) -> MEDICINE
                context.getString(R.string.nature) -> NATURE
                context.getString(R.string.perception) -> PERCEPTION
                context.getString(R.string.performance) -> PERFORMANCE
                context.getString(R.string.persuasion) -> PERSUASION
                context.getString(R.string.religion) -> RELIGION
                context.getString(R.string.sleight_of_hand) -> SLEIGHT_OF_HAND
                context.getString(R.string.stealth) -> STEALTH
                context.getString(R.string.survival) -> SURVIVAL
                else -> UNDEFINED
            }
        }
        fun getNameFromSkill(skill: Skills, context: Context): String {
            return when (skill){
                ACROBATICS -> context.getString(R.string.acrobatics)
                ANIMAL_HANDLING -> context.getString(R.string.animal_handling)
                ARCANA -> context.getString(R.string.arcana)
                ATHLETICS -> context.getString(R.string.athletics)
                DECEPTION -> context.getString(R.string.deception)
                HISTORY -> context.getString(R.string.history)
                INSIGHT -> context.getString(R.string.insight)
                INTIMIDATION -> context.getString(R.string.intimidation)
                INVESTIGATION -> context.getString(R.string.investigation)
                MEDICINE -> context.getString(R.string.medicine)
                NATURE -> context.getString(R.string.nature)
                PERCEPTION -> context.getString(R.string.perception)
                PERFORMANCE -> context.getString(R.string.performance)
                PERSUASION -> context.getString(R.string.persuasion)
                RELIGION -> context.getString(R.string.religion)
                SLEIGHT_OF_HAND -> context.getString(R.string.sleight_of_hand)
                STEALTH -> context.getString(R.string.stealth)
                SURVIVAL -> context.getString(R.string.survival)
                UNDEFINED -> ""
            }
        }
    }
}