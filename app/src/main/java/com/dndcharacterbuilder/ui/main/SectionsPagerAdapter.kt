package com.dndcharacterbuilder.ui.main

import android.content.Context

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.dndcharacterbuilder.MainActivity
import com.dndcharacterbuilder.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val activity: AppCompatActivity, fm: FragmentManager)
    : FragmentStateAdapter(fm, activity.lifecycle) {
    val items = arrayOf(
        FragmentItem(
            BasicDataFragment()
        )
    )

    override fun createFragment(position: Int): Fragment {
        activity.supportFragmentManager.commit {
            detach(items[position].fragment)
            attach(items[position].fragment)
        }
        return items[position].fragment
    }

    fun getPageTitle(position: Int): String {
        return activity.resources.getString(TAB_TITLES[position])
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = items[position].id

    override fun containsItem(itemId: Long): Boolean = items.any { it.id == itemId }

    private var newId: Long = 0
    inner class FragmentItem(
        val fragment: Fragment
    ) {
        private val _id: Long = ++newId
        val id: Long
            get() {
                val prefs = activity.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
                val characterId = prefs.getInt(MainActivity.KEY_CHARACTER_ID, 0)
                return characterId * 100 + _id
            }
    }
}
