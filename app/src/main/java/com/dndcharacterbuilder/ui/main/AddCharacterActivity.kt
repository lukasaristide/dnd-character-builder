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

class AddCharacterActivity : AppCompatActivity() {

	private val binding: ActivityAddCharacterBinding by lazy {
		ActivityAddCharacterBinding.inflate(layoutInflater)
	}

	private val database: AppDatabase by lazy {
		Room.databaseBuilder(this, AppDatabase::class.java, AppDatabase.databaseName).build()
	}

	private val classesList: ListPopupWindow by lazy {
		val field = binding.classField
		val items = database.classDao().getAll().map { it.name }
		val popup = ListPopupWindow(this).apply {
			setAdapter(ArrayAdapter(this@AddCharacterActivity, R.layout.item_popup, items))
			anchorView = field
			isModal = true
			setOnItemClickListener { _, _, position, _ ->
				field.text = items[position]
				dismiss()
			}
		}
		field.apply {
			setOnClickListener { popup.show() }
			isFocusableInTouchMode = true
			setOnFocusChangeListener { _, hasFocus ->
				if (hasFocus) {
					popup.show()
				}
			}
		}
		popup
	}

	override fun onCreate (savedInstanceState: Bundle?): Unit {
		super.onCreate(savedInstanceState)
		setContentView(binding.root)

		// This field is initalized on first reference.
		// It cannot be initalized on the main thread as it uses database.
		thread {
			classesList
		}

		binding.cancelAddingCharacterButton.setOnClickListener {
			finish()
		}

		binding.addCharacterButton.setOnClickListener {
			thread {
				try {
					val character = Character(
						name = requireString(binding.nameField),

						race = requireString(binding.raceField),
						cclass = database.classDao().getDetail(requireString(binding.classField))!!.id,

						strength = requireInt(binding.strengthField),
						dexterity = requireInt(binding.dexterityField),
						constitution = requireInt(binding.constitutionField),
						intelligence = requireInt(binding.intelligenceField),
						wisdom = requireInt(binding.wisdomField),
						charisma = requireInt(binding.charismaField)
					)

					database.characterDao().insert(character)
					finish()
				} catch (e: RequirementNotMetException) {
					runOnUiThread {
						Toast.makeText(this, R.string.fill_necessary_fields_text, Toast.LENGTH_SHORT).show()
					}
				}
			}
		}
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
