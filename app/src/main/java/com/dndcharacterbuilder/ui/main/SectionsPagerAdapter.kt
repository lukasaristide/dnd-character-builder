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
        R.string.tab_text_2,
        R.string.tab_text_3
)

/**
 * A [FragmentStateAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val activity: AppCompatActivity, fm: FragmentManager)
    : FragmentStateAdapter(fm, activity.lifecycle) {
    val items = arrayOf(
        BasicDataFragment(),
        SkillsFragment(),
        SpellcastingFragment()
    )

    override fun createFragment(position: Int): Fragment {
        return items[position]
    }

    fun getPageTitle(position: Int): String {
        return activity.resources.getString(TAB_TITLES[position])
    }

    override fun getItemCount(): Int = items.size
}
