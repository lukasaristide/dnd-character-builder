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
    @Delete
    fun delete(characters: List<Character>)
	@Query("select * from Character where id = :id")
	fun getById(id: Int): Character?
    @Query("select * from Character")
    fun getAll(): List<Character>
    @Query("select * from CharacterInfo")
    fun getInfo(): List<CharacterInfo>
    @Query("select * from CharacterInfo where characterId = :id")
    fun getInfo(id: Int): CharacterInfo?
}
