package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.dndcharacterbuilder.database.Character
import com.dndcharacterbuilder.databinding.ItemCharacterCardBinding

class CharactersAdapter(
	private val context: Context,
	private val characters: List<Character>
) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {
	
	override fun getItemCount (): Int = characters.size

	override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
		return ViewHolder(ItemCharacterCardBinding.inflate(LayoutInflater.from(parent.context)))
	}

	override fun onBindViewHolder (holder: ViewHolder, position: Int) {
		holder.name.text = characters[position].name
	}
	
	inner class ViewHolder(binding: ItemCharacterCardBinding) : RecyclerView.ViewHolder(binding.root) {
		val name: TextView = binding.nameField

		init {
			binding.root.layoutParams = LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT
				).apply {
					// Order of margins: left, top, right, bottom
					setMargins(dpToPx(16), dpToPx(8), dpToPx(16), dpToPx(8))
				}
		}

		private fun dpToPx (dp: Int): Int
			= TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.getResources().getDisplayMetrics()).toInt()
	}

}
