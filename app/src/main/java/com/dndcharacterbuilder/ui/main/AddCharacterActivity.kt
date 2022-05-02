package com.dndcharacterbuilder.ui.main

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

import kotlin.concurrent.thread

import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.Character
import com.dndcharacterbuilder.database.CharacterDao
import com.dndcharacterbuilder.databinding.ActivityAddCharacterBinding

class AddCharacterActivity : AppCompatActivity() {

	private val binding: ActivityAddCharacterBinding by lazy {
		ActivityAddCharacterBinding.inflate(layoutInflater)
	}

	override fun onCreate (savedInstanceState: Bundle?): Unit {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		binding.cancelAddingCharacterButton.setOnClickListener {
			finish()
		}

		binding.addCharacterButton.setOnClickListener {
			try {
				val character = Character(
					name = requireString(binding.nameField),

					race = requireString(binding.raceField),
					cclass = requireString(binding.classField),

					strength = requireInt(binding.strengthField),
					dexterity = requireInt(binding.dexterityField),
					constitution = requireInt(binding.constitutionField),
					intelligence = requireInt(binding.intelligenceField),
					wisdom = requireInt(binding.wisdomField),
					charisma = requireInt(binding.charismaField)
				)

                val characterDao = Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.databaseName).build().characterDao()
				thread {
					characterDao.insert(character)
					finish()
				}
			} catch (e: RequirementNotMetException) {
				Toast.makeText(this, R.string.fill_necessary_fields_text, Toast.LENGTH_SHORT).show()
			}
		}
	}

	private fun require (view: EditText) {
		if (view.text.isEmpty()) {
			throw RequirementNotMetException()
		}
	}

	private fun requireString (view: EditText): String {
		require(view)
		return view.text.toString()
	}

	private fun requireInt (view: EditText): Int {
		require(view)
		return view.text.toString().toInt()
	}

	class RequirementNotMetException : Exception {
		constructor() : super()
		constructor(msg: String) : super(msg)
	}
}
