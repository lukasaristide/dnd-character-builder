package com.dndcharacterbuilder.ui.utils

fun getModifier(statValue: Int): Int {
    val modifier = statValue / 2 - 5
    return if (modifier >= 0)
        modifier
    else
        modifier
}

fun getProficiencyBonus(level: Int): Int{
    return if (level < 5) 2
    else if (level < 9) 3
    else if (level < 13) 4
    else if (level < 17) 5
    else 6
}