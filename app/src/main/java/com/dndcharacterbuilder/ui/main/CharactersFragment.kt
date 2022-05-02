package com.dndcharacterbuilder.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import com.dndcharacterbuilder.databinding.FragmentCharactersBinding

class CharactersFragment : Fragment() {

    private lateinit var binding: FragmentCharactersBinding

    override fun onCreateView (inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCharactersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated (view: View, savedInstanceState: Bundle?) {
        binding.addCharacterButton.setOnClickListener {
            startActivity(Intent(context, AddCharacterActivity::class.java))
        }
    }

}
