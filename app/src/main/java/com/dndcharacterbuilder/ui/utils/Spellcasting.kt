package com.dndcharacterbuilder.ui.utils

fun getSpellslotsForLevelFull(level: Int, circle: Int) : Int {
    return when(circle){
        1 -> {
            when(level){
                1 -> 2
                2 -> 3
                else -> 4
            }
        }
        2 -> {
            if(level < 3) 0
            when(level){
                3 -> 2
                else -> 3
            }
        }
        3 -> {
            if(level < 5) 0
            when(level){
                5 -> 2
                else -> 3
            }
        }
        4 -> {
            if(level < 7) 0
            when(level){
                7 -> 1
                8 -> 2
                else -> 3
            }
        }
        5 -> {
            if (level < 9) 0
            else if (level == 9) 1
            else if (level < 18) 2
            else 3
        }
        6 -> {
            if (level < 11) 0
            else if (level < 19) 1
            else 2
        }
        7 -> {
            if (level < 13) 0
            else if (level < 20) 1
            else 2
        }
        8 -> {
            if (level < 15) 0
            else 1
        }
        9 -> {
            if (level < 17) 0
            else 1
        }
        else -> 0
    }
}
fun getSpellslotsForLevelPact(level: Int, circle: Int) : Int {
    return if (circle != level/2) 0
    else if (level < 2) 1
    else if (level < 11) 2
    else if (level < 17) 3
    else 4
}
fun getSpellslotsForLevel12(level: Int, circle: Int) : Int {
    return when(circle){
        1 -> {
            if (level < 2) 0
            else if (level < 3) 2
            else if (level < 5) 3
            else 4
        }
        2 -> {
            if (level < 5) 0
            else if (level < 7) 2
            else 3
        }
        3 -> {
            if (level < 9) 0
            else if (level < 11) 2
            else 3
        }
        4 -> {
            if (level < 13) 0
            else if (level < 15) 1
            else if (level < 17) 2
            else 3
        }
        5 -> {
            if (level < 17) 0
            else if (level < 19) 1
            else 2
        }
        else -> 0
    }
}
fun getSpellslotsForLevel13(level: Int, circle: Int) : Int {
    return when(circle){
        1 -> {
            if (level < 3) 2
            else if (level < 5) 3
            else 4
        }
        2 -> {
            if (level < 5) 0
            else if (level < 7) 2
            else 3
        }
        3 -> {
            if (level < 9) 0
            else if (level < 11) 2
            else 3
        }
        4 -> {
            if (level < 13) 0
            else if (level < 15) 1
            else if (level < 17) 2
            else 3
        }
        5 -> {
            if (level < 17) 0
            else if (level < 19) 1
            else 2
        }
        else -> 0
    }
}

fun getSpellslotsForLevel(type: String, level: Int, circle: Int) : Int {
    return when(type){
        "full" -> getSpellslotsForLevelFull(level, circle)
        "pact" -> getSpellslotsForLevelPact(level, circle)
        "1/2" -> getSpellslotsForLevel12(level, circle)
        "1/3" -> getSpellslotsForLevel13(level, circle)
        else -> 0
    }
}