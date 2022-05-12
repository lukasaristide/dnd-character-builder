package com.dndcharacterbuilder.database

import androidx.room.DatabaseView

@DatabaseView(
    """
    select c.name, c.strength, c.dexterity, c.constitution, c.intelligence, c.wisdom, c.charisma, r.name as race, k.name as cclass
    from character as c
    inner join klass as k on k.id = c.cclass
    inner join race as r on r.id = c.race
    """
)
data class CharacterInfo(
    val name: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int,
    val race: String,
    val cclass: String
)