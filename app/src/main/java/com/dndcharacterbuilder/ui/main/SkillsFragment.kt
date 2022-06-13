package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TableRow
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.view.get
import androidx.core.view.iterator
import androidx.core.view.size

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

import com.dndcharacterbuilder.MainActivity
import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.databinding.FragmentBasicDataBinding
import com.dndcharacterbuilder.databinding.FragmentSkillsBinding
import com.dndcharacterbuilder.databinding.SkillsTableRowBinding
import com.dndcharacterbuilder.ui.utils.Skills
import com.dndcharacterbuilder.ui.utils.getModifier
import com.dndcharacterbuilder.ui.utils.getProficiencyBonus
import kotlin.concurrent.thread

class SkillsFragment : Fragment() {
    private var _binding: FragmentSkillsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSkillsBinding.inflate(inflater, container, false)

        val prefs = if (activity != null) {
            activity!!.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        } else return binding.root

        val id = prefs.getInt(MainActivity.KEY_CHARACTER_ID, 0)

        thread {
            val database: AppDatabase by lazy {
                Room.databaseBuilder(
                    requireContext(),
                    AppDatabase::class.java,
                    AppDatabase.databaseName
                ).build()
            }
            val characterInfo = database.characterDao().getInfo(id) ?: return@thread
            for (skill in Skills.values()){
                if (skill == Skills.UNDEFINED)
                    break
                binding.skillsTable.addView(layoutInflater.inflate(R.layout.skills_table_row, null))
                ((binding.skillsTable[binding.skillsTable.size-1] as TableRow)[1] as TextView).text =
                    Skills.getNameFromSkill(skill, requireContext())
            }
            for (row in binding.skillsTable){
                val tableRow = row as TableRow
                val name = (tableRow[1] as TextView).text.toString()
                val skill = Skills.getSkillFromName(name, requireContext())
                for (i in listOf(0, 1, 2)) {
                    ((tableRow[2] as RadioGroup)[i] as RadioButton).setOnClickListener { _ ->
                        val modifier = characterInfo.getSkillModifier(skill) +
                                getProficiencyBonus(characterInfo.level) * i
                        (tableRow[0] as TextView).text =
                            if (modifier < 0) "$modifier"
                            else "+$modifier"
                        val idPrefs = id.toString() + name
                        Log.d("PREFS ADD", idPrefs)
                        prefs.edit().putInt(idPrefs, i).apply()
                    }
                }
                val proficiencyBonus = prefs.getInt(id.toString() + name, 0)
                var button = ((tableRow[2] as RadioGroup)[proficiencyBonus] as RadioButton)
                button.callOnClick()
                button.isChecked = true
            }
        }.join()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
