package com.dndcharacterbuilder.ui.utils


object SkillToAbility {
    val strSkills = hashSetOf<Skills>(Skills.ATHLETICS)
    val dexSkills = hashSetOf<Skills>(Skills.ACROBATICS, Skills.SLEIGHT_OF_HAND, Skills.STEALTH)
    val conSkills = hashSetOf<Skills>()
    val intSkills = hashSetOf<Skills>(Skills.ARCANA, Skills.HISTORY, Skills.INVESTIGATION,
        Skills.NATURE, Skills.RELIGION)
    val wisSkills = hashSetOf<Skills>(Skills.ANIMAL_HANDLING, Skills.INSIGHT, Skills.MEDICINE,
        Skills.PERCEPTION, Skills.SURVIVAL)
    val chaSkills = hashSetOf<Skills>(Skills.DECEPTION, Skills.INTIMIDATION, Skills.PERFORMANCE,
        Skills.PERSUASION)
}