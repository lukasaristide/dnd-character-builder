package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.dndcharacterbuilder.databinding.FragmentSpellcastingBinding
import com.dndcharacterbuilder.databinding.SkillsTableRowBinding
import com.dndcharacterbuilder.ui.createPopupList
import com.dndcharacterbuilder.ui.utils.Skills
import com.dndcharacterbuilder.ui.utils.getModifier
import com.dndcharacterbuilder.ui.utils.getProficiencyBonus
import com.dndcharacterbuilder.ui.utils.getSpellslotsForLevel
import kotlin.concurrent.thread

class SpellcastingFragment : Fragment() {
    private var _binding: FragmentSpellcastingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpellcastingBinding.inflate(inflater, container, false)

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
            val characterClass = database.classDao().getDetail(characterInfo.cclass) ?: return@thread
            activity!!.runOnUiThread {
                context?.let { context ->
                    createPopupList(binding.circle1Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 1))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle2Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 2))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle3Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 3))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle4Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 4))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle5Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 5))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle6Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 6))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle7Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 7))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle8Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 8))
                            .map { it.toString() },
                        context
                    )
                    createPopupList(binding.circle9Number,
                        (1..getSpellslotsForLevel(characterClass.casterProgression, characterInfo.level, 9))
                            .map { it.toString() },
                        context
                    )
                    for (i in arrayOf(binding.circle1Number, binding.circle2Number, binding.circle3Number,
                        binding.circle4Number, binding.circle5Number, binding.circle6Number, binding.circle7Number,
                        binding.circle8Number, binding.circle9Number))
                    {
                        val prefsId = "${characterInfo.characterId.toString()}#${i.id.toString()}"
                        i.text = prefs.getString(prefsId, "")
                        i.addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {
                                prefs.edit()
                                    .putString(prefsId, s.toString())
                                    .apply()
                                Log.d("Changed", "$prefsId ${s.toString()}")
                            }
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            }
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            }
                        })
                    }
                }
            }
            database.close()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
