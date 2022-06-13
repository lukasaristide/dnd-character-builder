package com.dndcharacterbuilder.ui.main

import android.annotation.SuppressLint
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
import com.dndcharacterbuilder.ui.utils.getStrModifier
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
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val id = if (activity != null) {
			val prefs = activity!!.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
			prefs.getInt(MainActivity.KEY_CHARACTER_ID, 0)
		} else return

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
				activity!!.runOnUiThread {
					if (_binding == null) return@runOnUiThread
					binding.name.setText(R.string.no_character_selected_text)
					binding.race.text = "---"
					binding.classAndLevel.text = "---"
					binding.strengthVal.text = ""
					binding.dexterityVal.text = ""
					binding.constitutionVal.text = ""
					binding.intelligenceVal.text = ""
					binding.charismaVal.text = ""
				}
				return@thread
			}

			activity!!.runOnUiThread {
				if (_binding == null) return@runOnUiThread
				binding.name.text = characterInfo.name
				binding.race.text = characterInfo.race
				// Is there a way to apply a single annotation to a block of code?
				// In this case, is there a reason to resolve the warning in another way?
				@SuppressLint("SetTextI18n")
				binding.classAndLevel.text = "${characterInfo.cclass} (${characterInfo.level})"
				@SuppressLint("SetTextI18n")
				binding.strengthVal.text = "${characterInfo.strength} (${getModifier(characterInfo.strength)})"
				@SuppressLint("SetTextI18n")
				binding.dexterityVal.text = "${characterInfo.dexterity} (${getModifier(characterInfo.dexterity)})"
				@SuppressLint("SetTextI18n")
				binding.constitutionVal.text = "${characterInfo.constitution} (${getModifier(characterInfo.constitution)})"
				@SuppressLint("SetTextI18n")
				binding.intelligenceVal.text = "${characterInfo.intelligence} (${getModifier(characterInfo.intelligence)})"
				@SuppressLint("SetTextI18n")
				binding.wisdomVal.text = "${characterInfo.wisdom} (${getModifier(characterInfo.wisdom)})"
				@SuppressLint("SetTextI18n")
				binding.charismaVal.text = "${characterInfo.charisma} (${getModifier(characterInfo.charisma)})"
			}
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}
