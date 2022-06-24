package com.dndcharacterbuilder.ui.main

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.room.Room

import kotlin.concurrent.thread

import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.Character
import com.dndcharacterbuilder.databinding.ActivityAddCharacterBinding
import com.dndcharacterbuilder.ui.createPopupList

class AddEditCharacterActivity : AppCompatActivity() {
	companion object {
		const val EXTRA_ID: String = "characterId"
		private const val BUNDLE_KEY_CLASS: String = "class"
		private const val BUNDLE_KEY_RACE: String = "race"
	}

	private val binding: ActivityAddCharacterBinding by lazy {
		ActivityAddCharacterBinding.inflate(layoutInflater)
	}

	private val database: AppDatabase by lazy {
		Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.databaseName).build()
	}

	private lateinit var classesList: ListPopupWindow
	private lateinit var racesList: ListPopupWindow

	private val characterId: Int by lazy {
		intent.getIntExtra(EXTRA_ID, 0)
	}

	override fun onCreate (savedInstanceState: Bundle?): Unit {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		thread {
			val classItems = database.classDao().getAll().map { it.name }
			val raceItems = database.raceDao().getAll().map { it.name }
			runOnUiThread {
				classesList = createPopupList(binding.classField, classItems, this)
				racesList = createPopupList(binding.raceField, raceItems, this)
			}
		}

		if (savedInstanceState != null) {
			binding.raceField.text = savedInstanceState.getCharSequence(BUNDLE_KEY_RACE) ?: ""
			binding.classField.text = savedInstanceState.getCharSequence(BUNDLE_KEY_CLASS) ?: ""
		}
		else if (characterId != 0) {
			thread {
				val characterInfo = database.characterDao().getInfo(characterId) ?: return@thread
				runOnUiThread {
					binding.nameField.setText(characterInfo.name)
					binding.levelField.setText(characterInfo.level.toString())

					binding.raceField.text = characterInfo.race
					binding.classField.text = characterInfo.cclass

					binding.strengthField.setText(characterInfo.strength.toString())
					binding.dexterityField.setText(characterInfo.dexterity.toString())
					binding.constitutionField.setText(characterInfo.constitution.toString())
					binding.intelligenceField.setText(characterInfo.intelligence.toString())
					binding.wisdomField.setText(characterInfo.wisdom.toString())
					binding.charismaField.setText(characterInfo.charisma.toString())
				}
			}
		}

		binding.cancelAddingCharacterButton.setOnClickListener {
			finish()
		}

		binding.addCharacterButton.setOnClickListener {
			thread {
				try {
					val character = Character(
						name = requireString(binding.nameField),
						level = requireInt(binding.levelField),

						race = database.raceDao().getDetail(requireString(binding.raceField))!!.id,
						cclass = database.classDao().getDetail(requireString(binding.classField))!!.id,

						strength = requireInt(binding.strengthField),
						dexterity = requireInt(binding.dexterityField),
						constitution = requireInt(binding.constitutionField),
						intelligence = requireInt(binding.intelligenceField),
						wisdom = requireInt(binding.wisdomField),
						charisma = requireInt(binding.charismaField)
					)

					if (characterId == 0) {
						database.characterDao().insert(character)
					}
					else {
						character.id = characterId
						database.characterDao().update(character)
					}
					finish()
				} catch (e: RequirementNotMetException) {
					runOnUiThread {
						Toast.makeText(this, R.string.fill_necessary_fields_text, Toast.LENGTH_SHORT).show()
					}
				}
			}
		}
	}

	override fun onSaveInstanceState (savedInstanceState: Bundle) {
		super.onSaveInstanceState(savedInstanceState)
		savedInstanceState.putCharSequence(BUNDLE_KEY_RACE, binding.raceField.text)
		savedInstanceState.putCharSequence(BUNDLE_KEY_CLASS, binding.classField.text)
	}

	private fun require (view: TextView) {
		if (view.text.isEmpty()) {
			throw RequirementNotMetException()
		}
	}

	private fun requireString (view: TextView): String {
		require(view)
		return view.text.toString()
	}

	private fun requireInt (view: TextView): Int {
		require(view)
		return view.text.toString().toInt()
	}

	class RequirementNotMetException : Exception {
		constructor() : super()
		constructor(msg: String) : super(msg)
	}
}
