package com.dndcharacterbuilder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(klass: Class)
    @Update
    fun update(klass: Class)
    @Delete
    fun delete(klass: Class)
    @Query("select * from klass")
    fun getAll(): List<Class>
    @Query("select * from klass where name = :name")
    fun getDetail(name: String): Class?
}
