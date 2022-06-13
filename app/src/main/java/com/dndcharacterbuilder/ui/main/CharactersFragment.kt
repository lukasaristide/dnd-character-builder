package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.room.Room

import kotlin.concurrent.thread

import com.dndcharacterbuilder.MainActivity
import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.CharacterDao
import com.dndcharacterbuilder.database.CharacterInfo
import com.dndcharacterbuilder.databinding.FragmentCharactersBinding
import com.dndcharacterbuilder.ui.utils.Skills

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding
    private var characterDao: CharacterDao? = null
    private val handler: Handler = Handler(Looper.getMainLooper())

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCharactersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listCharacters()
    }

    override fun onResume () {
        super.onResume()
        listCharacters()
    }

    private fun listCharacters () {
        context?.let {
            if (characterDao == null) {
                characterDao = Room.databaseBuilder(it, AppDatabase::class.java, AppDatabase.databaseName).build().characterDao()
            }
            thread {
                val characters = characterDao!!.getInfo()
                runOnUiThread {
                    binding.charactersList.adapter = CharactersAdapter(
                        it,
                        characters,
                        object : CharactersAdapter.OnItemClickListener {
                            override fun addCharacter() {
                                startActivity(Intent(context, AddEditCharacterActivity::class.java))
                            }

							override fun onCharacterChosen(characterId: Int) {
								activity?.let { activity ->
									val prefs = activity.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE)
									prefs.edit().putInt(MainActivity.KEY_CHARACTER_ID, characterId).apply()
									if (activity is MainActivity) {
										activity.notifyCharacterChanged()
									}
								}
							}
                        },
                        object : CharactersAdapter.OnMenuItemClickListener {
                            override fun delete(characterInfo: CharacterInfo) {
                                thread {
                                    val character = characterDao!!.getById(characterInfo.characterId)
                                    if (character != null) {
                                        val prefsEditor = context!!.getSharedPreferences(MainActivity.SHARED_PREFS_NAME,
                                            Context.MODE_PRIVATE).edit()
                                        prefsEditor.remove(character.id.toString())
                                        for (skill in Skills.values()){
                                            val idPrefs = character.id.toString() + Skills.getNameFromSkill(skill, context!!)
                                            Log.d("PREFS RM", idPrefs)
                                            prefsEditor.remove(idPrefs).apply()
                                        }
                                        characterDao!!.delete(character)
                                    }
                                    runOnUiThread {
                                        //TODO At this point happens exactly the same thing as
                                        // all the previous problems with FAB background.
                                        listCharacters()
                                    }
                                }
                            }

                            override fun edit(characterInfo: CharacterInfo) {
                                val intent = Intent(context, AddEditCharacterActivity::class.java)
                                intent.putExtra(AddEditCharacterActivity.EXTRA_ID, characterInfo.characterId)
                                startActivity(intent)
                            }
                        }
                    )
                }
            }
        }
    }

    private fun runOnUiThread (action: () -> Unit) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            handler.post(action)
        }
        else {
            action.invoke()
        }
    }

}
