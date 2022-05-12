package com.dndcharacterbuilder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface RaceDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(race: Race)
    @Update
    fun update(race: Race)
    @Delete
    fun delete(race: Race)
    @Query("select * from race")
    fun getAll(): List<Race>
    @Query("select * from race where name = :name")
    fun getDetail(name: String): Race?
}
