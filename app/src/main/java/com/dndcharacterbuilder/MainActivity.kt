package com.dndcharacterbuilder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.dndcharacterbuilder.databinding.ActivityMainBinding
import com.dndcharacterbuilder.ui.main.CharactersFragment
import com.dndcharacterbuilder.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

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
        TabLayoutMediator(tabs, viewPager, { tab, positon ->
            tab.text = sectionsPagerAdapter.getPageTitle(positon)
        }).attach()
        val fab: FloatingActionButton = binding.fab

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CharactersFragment>(R.id.characters_frame)
        }

        fab.setOnClickListener {
            fab.setExpanded(true)
        }
    }

    override fun onBackPressed(): Unit {
        if (binding.fab.isExpanded()) {
            binding.fab.setExpanded(false)
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
        return when(item.itemId) {
            R.id.import_item -> {
                Toast.makeText(this, "Implement me!", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
