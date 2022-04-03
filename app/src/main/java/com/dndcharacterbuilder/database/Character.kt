package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String
)
