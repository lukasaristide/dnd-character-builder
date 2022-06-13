package com.dndcharacterbuilder.ui.utils

fun getModifier(statValue: Int): String {
    val modifier = statValue / 2 - 5
    return if (modifier >= 0)
        "+$modifier"
    else
        "$modifier"
}
