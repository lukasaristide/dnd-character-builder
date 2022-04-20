package com.dndcharacterbuilder

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import com.dndcharacterbuilder.databinding.ActivityMainBinding
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

        fab.setOnClickListener {
            fab.setExpanded(true)
            binding.tabs.visibility = View.GONE
            binding.charactersList.setEnabled(true)
            viewPager.setUserInputEnabled(false)
        }
    }

    override fun onBackPressed(): Unit {
        if (binding.fab.isExpanded()) {
            binding.fab.setExpanded(false)
            binding.tabs.visibility = View.VISIBLE
            binding.charactersList.setEnabled(false)
            binding.viewPager.setUserInputEnabled(true)
        }
        else {
            super.onBackPressed()
        }
    }
}
