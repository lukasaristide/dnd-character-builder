package com.dndcharacterbuilder.ui.main

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.dndcharacterbuilder.databinding.ActivityAddCharacterBinding

class AddCharacterActivity : AppCompatActivity() {

    private val binding: ActivityAddCharacterBinding by lazy {
        ActivityAddCharacterBinding.inflate(layoutInflater)
    }

    override fun onCreate (savedInstanceState: Bundle?): Unit {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}
