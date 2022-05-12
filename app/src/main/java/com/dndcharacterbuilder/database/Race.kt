package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
data class Race(
    var name: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
