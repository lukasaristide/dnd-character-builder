package com.dndcharacterbuilder.database

fun clearDB(database: AppDatabase){
    val characterDao = database.characterDao()
    characterDao.delete(characterDao.getAll())
    val raceDao = database.raceDao()
    raceDao.delete(raceDao.getAll())
    val classDao = database.classDao()
    classDao.delete(classDao.getAll())
}
