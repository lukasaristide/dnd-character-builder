package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    indices = [Index(value = ["name"], unique = true)]
)
@Serializable
data class Race(
    @SerialName("name")
    var name: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
