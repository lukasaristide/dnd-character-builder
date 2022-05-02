package com.dndcharacterbuilder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        const val databaseName = "app-database"
    }

    abstract fun characterDao (): CharacterDao
}
