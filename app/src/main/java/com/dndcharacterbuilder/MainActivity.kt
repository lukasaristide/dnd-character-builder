package com.dndcharacterbuilder

import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.room.Room
import androidx.viewpager2.widget.ViewPager2
import com.dndcharacterbuilder.database.*

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import kotlin.concurrent.thread

import com.dndcharacterbuilder.databinding.ActivityMainBinding
import com.dndcharacterbuilder.ui.bitmaputils.*
import com.dndcharacterbuilder.ui.main.CharactersFragment
import com.dndcharacterbuilder.ui.main.SectionsPagerAdapter

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
                val urlAddress = dialogLayout.findViewById<EditText>(R.id.request_url_text)

                val database: AppDatabase by lazy {
                    Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.databaseName)
                        .build()
                }
                val messagePrinter = { message: String ->
                    runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
                }
                with (builder) {
                    setTitle("Provide URL for data import:")
                    setPositiveButton("OK") { _, _ ->
                        importDB(database, urlAddress.text.toString())
                        messagePrinter("Database import done!")
                    }
                    setNegativeButton("Cancel") { _, _ -> }
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
                    clearDB(database)
                    runOnUiThread { Toast.makeText(this, "Database cleared!", Toast.LENGTH_SHORT).show() }
                    getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().clear().apply()
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
        if (fab.isExpanded) {
            return
        }
        // Hide fab so it is not included in the blurred background
        fab.visibility = View.GONE
        supportFragmentManager.findFragmentByTag(charactersFragmentTag)?.getView()?.background = BitmapDrawable(
            resources,
            getBitmapFromView(binding.root)
                .blur(this@MainActivity, 5f)
                .bleach(140)
        )
        fab.visibility = View.VISIBLE
        binding.root.invalidate()
        fab.isExpanded = true
        // TODO The programmatically set background does not behave
        // as expected and thus the need. How to fix this?
        // Problem: the bitmap appears behind all views, not as
        // background of the view. When testing ColorDrawable instead,
        // nothing happens.
        binding.tabs.visibility = View.INVISIBLE
        binding.viewPager.visibility = View.INVISIBLE
    }

    private fun closeFab(fab: FloatingActionButton) {
        if (!fab.isExpanded) {
            return
        }
        binding.tabs.visibility = View.VISIBLE
        binding.viewPager.visibility = View.VISIBLE
        binding.fab.isExpanded = false
        supportFragmentManager.findFragmentByTag(charactersFragmentTag)?.getView()?.background = null
        recreate()
    }
}
