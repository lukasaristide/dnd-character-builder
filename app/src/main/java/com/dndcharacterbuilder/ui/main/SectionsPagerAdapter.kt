package com.dndcharacterbuilder.ui.main

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter

import com.dndcharacterbuilder.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val activity: ComponentActivity, fm: FragmentManager)
    : FragmentStateAdapter(fm, activity.lifecycle) {

    override fun createFragment(position: Int): Fragment {
        // createFragment is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return PlaceholderFragment.newInstance(position + 1)
    }

    fun getPageTitle(position: Int): String {
        return activity.resources.getString(TAB_TITLES[position])
    }

    override fun getItemCount(): Int {
        // Show 2 total pages.
        return 2
    }
}
