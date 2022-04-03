package com.dndcharacterbuilder.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Character::class], version = 1)
abstract class AppDatabase : RoomDatabase() { }
