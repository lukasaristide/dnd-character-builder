package com.dndcharacterbuilder.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.room.Room

import kotlin.concurrent.thread

import com.dndcharacterbuilder.database.AppDatabase
import com.dndcharacterbuilder.database.CharacterDao
import com.dndcharacterbuilder.database.CharacterInfo
import com.dndcharacterbuilder.databinding.FragmentCharactersBinding

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
                        object : CharactersAdapter.OnAddCharacterListener {
                            override fun onClick() {
                                startActivity(Intent(context, AddCharacterActivity::class.java))
                            }
                        },
						object : CharactersAdapter.OnMenuItemClickListener {
							override fun delete(characterInfo: CharacterInfo) {
								thread {
									characterDao!!.delete(characterDao!!.getById(characterInfo.characterId))
									runOnUiThread {
										//TODO At this point happens exactly the same thing as
										// all the previous problems with FAB background.
										listCharacters()
									}
								}
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
