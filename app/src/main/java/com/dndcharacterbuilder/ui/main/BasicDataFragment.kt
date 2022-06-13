package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room

import com.dndcharacterbuilder.MainActivity
import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.databinding.FragmentBasicDataBinding
import com.dndcharacterbuilder.ui.utils.getModifier
import kotlin.concurrent.thread

class BasicDataFragment : Fragment() {
	private var _binding: FragmentBasicDataBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		_binding = FragmentBasicDataBinding.inflate(inflater, container, false)

		val id = if (activity != null) {
			val prefs = activity!!.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
			prefs.getInt(MainActivity.KEY_CHARACTER_ID, 0)
		} else return binding.root

		thread {
			val database: AppDatabase by lazy {
				Room.databaseBuilder(
					requireContext(),
					AppDatabase::class.java,
					AppDatabase.databaseName
				).build()
			}
			val characterInfo = database.characterDao().getInfo(id)
			if (characterInfo == null){
				binding.name.text = "(no character selected)"
				binding.race.text = "---"
				binding.classAndLevel.text = "---"
				binding.strengthVal.text = ""
				binding.dexterityVal.text = ""
				binding.constitutionVal.text = ""
				binding.intelligenceVal.text = ""
				binding.charismaVal.text = ""
				return@thread
			}
			binding.name.text = characterInfo.name
			binding.race.text = characterInfo.race
			binding.classAndLevel.text = "${characterInfo.cclass} (${characterInfo.level})"
			binding.strengthVal.text = "${characterInfo.strength} (${getModifier(characterInfo.strength)})"
			binding.dexterityVal.text = "${characterInfo.dexterity} (${getModifier(characterInfo.dexterity)})"
			binding.constitutionVal.text = "${characterInfo.constitution} (${getModifier(characterInfo.constitution)})"
			binding.intelligenceVal.text = "${characterInfo.intelligence} (${getModifier(characterInfo.intelligence)})"
			binding.wisdomVal.text = "${characterInfo.wisdom} (${getModifier(characterInfo.wisdom)})"
			binding.charismaVal.text = "${characterInfo.charisma} (${getModifier(characterInfo.charisma)})"
		}.join()

		return binding.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
