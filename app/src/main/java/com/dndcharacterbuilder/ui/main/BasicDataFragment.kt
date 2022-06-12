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

import com.dndcharacterbuilder.MainActivity
import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.databinding.FragmentBasicDataBinding

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
		} else 0
		binding.textView.text = "Current character id: ${id}"

		return binding.root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}