package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    var name: String,
    var strength: Int,
    var dexterity: Int,
    var constitution: Int,
    var intelligence: Int,
    var wisdom: Int,
    var charisma: Int,
    var race: String,
    var cclass: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
