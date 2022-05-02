package com.dndcharacterbuilder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CharacterDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(character: Character)
    @Update
    fun update(character: Character)
    @Delete
    fun delete(character: Character)
    @Query("select * from character")
    fun getAll(): List<Character>
}
