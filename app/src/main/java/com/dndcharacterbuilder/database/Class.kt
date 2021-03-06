package com.dndcharacterbuilder.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(
    tableName = "klass",
    indices = [Index(value = ["name"], unique = true)]
)
@Serializable
data class Class(
    @SerialName("name")
    var name: String,
    @SerialName("casterProgression")
    var casterProgression : String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
