package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Class::class,
            parentColumns = ["id"],
            childColumns = ["cclass"]
        )],
    indices = [Index(value = ["cclass"])]
)
data class Character(
    var name: String,
    var strength: Int,
    var dexterity: Int,
    var constitution: Int,
    var intelligence: Int,
    var wisdom: Int,
    var charisma: Int,
    var race: String,
    var cclass: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
