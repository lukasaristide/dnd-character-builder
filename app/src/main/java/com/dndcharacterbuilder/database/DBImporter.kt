package com.dndcharacterbuilder.database

import android.util.Log
import com.dndcharacterbuilder.jsonloader.GetClass
import com.dndcharacterbuilder.jsonloader.GetClassFileNamesFromIndexFile
import com.dndcharacterbuilder.jsonloader.GetRaces
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.net.URL
import java.nio.charset.Charset
import kotlin.concurrent.thread

fun importDB(database: AppDatabase, urlBase: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
    Log.d("URL", "Got URL: $urlBase")
    thread {
        try {
            val charsetForFetch = Charset.forName("US-ASCII")
            Log.d("GET FILENAME", DBImportConsts.RACES_FILENAME)
            val racesContent = URL(urlBase + DBImportConsts.RACES_FILENAME).readText(charsetForFetch)
            val jsons = GetRaces(racesContent)
            for (race in jsons){
                Log.d("DB", race)
                database.raceDao().insert(Json.decodeFromString<Race>(race))
            }

            val urlBaseForClass = urlBase + DBImportConsts.CLASS_DIR
            Log.d("GET FILENAME", DBImportConsts.INDEX_CLASSES_FILENAME)
            val classIndexContent = URL(urlBaseForClass + DBImportConsts.INDEX_CLASSES_FILENAME).readText(charsetForFetch)
            val classFileNames = GetClassFileNamesFromIndexFile(classIndexContent)
            for (filename in classFileNames){
                Log.d("GET FILENAME", filename)
                val classContent = URL(urlBaseForClass + filename).readText(charsetForFetch)
                val parsedClass = GetClass(classContent)
                if (parsedClass == "") {
                    Log.d("GET FILENAME", "$filename skipped")
                    continue
                }
                database.classDao().insert(Json.decodeFromString<Class>(parsedClass))
            }
            onSuccess()
            database.close()
        }
        catch (e : Exception){
            Log.d("ERR", e.toString())
            onFailure()
        }
    }
}
