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
        ),
        ForeignKey(
            entity = Race::class,
            parentColumns = ["id"],
            childColumns = ["race"]
        )],
    indices = [
        Index(value = ["cclass"]),
        Index(value = ["race"])
        ]
)
data class Character(
    var name: String,
    var level: Int,
    var strength: Int,
    var dexterity: Int,
    var constitution: Int,
    var intelligence: Int,
    var wisdom: Int,
    var charisma: Int,
    var race: Int,
    var cclass: Int
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
