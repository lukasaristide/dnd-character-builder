package com.dndcharacterbuilder

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import java.lang.Exception
import java.net.URL

import kotlin.concurrent.thread

import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import com.dndcharacterbuilder.databinding.ActivityMainBinding
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.Class
import com.dndcharacterbuilder.database.Race
import com.dndcharacterbuilder.jsonloader.GetClass
import com.dndcharacterbuilder.jsonloader.GetClassFileNamesFromIndexFile
import com.dndcharacterbuilder.jsonloader.GetRaces
import com.dndcharacterbuilder.ui.bitmaputils.*
import com.dndcharacterbuilder.ui.main.CharactersFragment
import com.dndcharacterbuilder.ui.main.SectionsPagerAdapter
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("dndcharacterbuilder")
        }

        const val KEY_CHARACTER_ID = "characterId"
        const val SHARED_PREFS_NAME = "preferences"

        const val charactersFragmentTag: String = "characters"
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val sectionsPagerAdapter by lazy {
        SectionsPagerAdapter(this, supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
        }.attach()
        val fab: FloatingActionButton = binding.fab

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CharactersFragment>(R.id.characters_frame, charactersFragmentTag)
            }
        }

        fab.setOnClickListener {
            expandFab(fab)
        }
    }

    override fun onResume(): Unit {
        super.onResume()
        closeFab(binding.fab)
    }

    override fun onBackPressed(): Unit {
        if (binding.fab.isExpanded) {
            closeFab(binding.fab)
        }
        else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.import_item -> {
                val builder = AlertDialog.Builder(this)
                val dialogLayout = layoutInflater.inflate(R.layout.request_url, null)
                val urlAdress = dialogLayout.findViewById<EditText>(R.id.request_url_text)

                val database: AppDatabase by lazy {
                    Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.databaseName)
                        .build()
                }
                with (builder) {
                    setTitle("Provide URL for data import:")
                    setPositiveButton("OK") { dialog, which ->
                        Log.d("URL", "Got URL: " + urlAdress.text.toString())
                        val urlBase = urlAdress.text.toString()
                        try {
                            thread {
                                val charsetForFetch = Charset.forName("US-ASCII")
                                Log.d("GET FILENAME", "races.json")
                                val racesContent = URL(urlBase + "races.json").readText(charsetForFetch)
                                val jsons = GetRaces(racesContent)
                                for (race in jsons){
                                    Log.d("DB", race)
                                    database.raceDao().insert(Json.decodeFromString<Race>(race))
                                }

                                val urlBaseForClass = urlBase + "class/"
                                Log.d("GET FILENAME", "index.json")
                                val classIndexContent = URL(urlBaseForClass + "index.json").readText(charsetForFetch)
                                val classFileNames = GetClassFileNamesFromIndexFile(classIndexContent)
                                for (filename in classFileNames){
                                    Log.d("GET FILENAME", filename)
                                    val classContent = URL(urlBaseForClass + filename).readText(charsetForFetch)
                                    val parsedClass = GetClass(classContent)
                                    if (parsedClass == "")
                                        continue
                                    database.classDao().insert(Json.decodeFromString<Class>(parsedClass))
                                }

                            }.join()
                        }
                        catch (e : Exception){
                            Log.d("ERR", e.toString())
                        }

                    }
                    setNegativeButton("Cancel") {dialog, which ->
                    }
                    setView(dialogLayout)
                    show()
                }
                true
            }
            R.id.clear_db -> {
                thread {
                    val database: AppDatabase by lazy {
                        Room.databaseBuilder(
                            this,
                            AppDatabase::class.java,
                            AppDatabase.databaseName
                        ).build()
                    }
                    val raceDao = database.raceDao()
                    raceDao.delete(raceDao.getAll())
                    val classDao = database.classDao()
                    classDao.delete(classDao.getAll())
                    val characterDao = database.characterDao()
                    characterDao.delete(characterDao.getAll())
                    runOnUiThread { Toast.makeText(this, "Database cleared!", Toast.LENGTH_SHORT).show() }
                }.join()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun notifyCharacterChanged() {
        sectionsPagerAdapter.notifyDataSetChanged()
        closeFab(binding.fab)
    }

    private fun expandFab(fab: FloatingActionButton) {
        if (fab.isExpanded()) {
            return
        }
        // Hide fab so it is not included in the blurred background
        fab.setVisibility(View.GONE)
        supportFragmentManager.findFragmentByTag(charactersFragmentTag)?.getView()?.background = BitmapDrawable(
            resources,
            getBitmapFromView(binding.root)
                .blur(this@MainActivity, 5f)
                .bleach(140)
        )
        fab.setVisibility(View.VISIBLE)
        binding.root.invalidate()
        fab.setExpanded(true)
        // TODO The programmatically set background does not behave
        // as expected and thus the need. How to fix this?
        // Problem: the bitmap appears behind all views, not as
        // background of the view. When testing ColorDrawable instead,
        // nothing happens.
        binding.tabs.setVisibility(View.INVISIBLE)
        binding.viewPager.setVisibility(View.INVISIBLE)
    }

    private fun closeFab(fab: FloatingActionButton) {
        if (!fab.isExpanded()) {
            return
        }
        binding.tabs.setVisibility(View.VISIBLE)
        binding.viewPager.setVisibility(View.VISIBLE)
        binding.fab.setExpanded(false)
        supportFragmentManager.findFragmentByTag(charactersFragmentTag)?.getView()?.background = null
        recreate()
    }
}
