package com.dndcharacterbuilder

import android.app.AlertDialog
import android.app.Dialog
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
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.Race

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.dndcharacterbuilder.databinding.ActivityMainBinding
import com.dndcharacterbuilder.jsonloader.GetRaces
import com.dndcharacterbuilder.ui.main.CharactersFragment
import com.dndcharacterbuilder.ui.main.SectionsPagerAdapter
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.awaitAll
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    companion object {
        init {
            System.loadLibrary("dndcharacterbuilder")
        }
    }

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = sectionsPagerAdapter.getPageTitle(position)
        }.attach()
        val fab: FloatingActionButton = binding.fab

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CharactersFragment>(R.id.characters_frame)
        }

        fab.setOnClickListener {
            fab.isExpanded = true
        }
    }

    override fun onBackPressed(): Unit {
        if (binding.fab.isExpanded) {
            binding.fab.isExpanded = false
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
                                val racesContent = URL(urlBase + "races.json").readText()
                                val jsons = GetRaces(racesContent)
                                for (race in jsons){
                                    Log.d("DB", race)
                                    database.raceDao().insert(Json.decodeFromString<Race>(race))
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
}
