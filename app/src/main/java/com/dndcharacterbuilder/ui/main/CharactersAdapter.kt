package com.dndcharacterbuilder.ui.main

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

import com.dndcharacterbuilder.R
import com.dndcharacterbuilder.database.Character
import com.dndcharacterbuilder.databinding.ItemAddCharacterCardBinding
import com.dndcharacterbuilder.databinding.ItemCharacterCardBinding

class CharactersAdapter(
	private val context: Context,
	private val characters: List<Character>,
	private val addListener: OnAddCharacterListener = object : OnAddCharacterListener { }
) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

	object ViewType {
		const val CHARACTER: Int = 0
		const val ADD: Int = 1
	}

	override fun getItemCount (): Int = characters.size + 1

	override fun getItemViewType (position: Int): Int
		= if (position < characters.size) ViewType.CHARACTER
		else ViewType.ADD

	override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ViewHolder {
		val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
		return when (viewType) {
			ViewType.CHARACTER -> CharacterViewHolder(ItemCharacterCardBinding.inflate(layoutInflater))
			ViewType.ADD -> AddCharacterViewHolder(ItemAddCharacterCardBinding.inflate(layoutInflater))
			else -> CharacterViewHolder(ItemCharacterCardBinding.inflate(layoutInflater))
		}
	}

	override fun onBindViewHolder (holder: ViewHolder, position: Int) {
		if (position < characters.size){
			(holder as CharacterViewHolder).name.text = characters[position].name
			holder.race.text = characters[position].race
			holder.cclass.text = characters[position].cclass
		}
	}

	inner abstract class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
		init {
			binding.root.layoutParams = LinearLayout.LayoutParams(
					// Order of dimensions: width, height
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

	inner class CharacterViewHolder(binding: ItemCharacterCardBinding) : ViewHolder(binding) {
		val name: TextView = binding.nameField
		val race: TextView = binding.raceField
		val cclass: TextView = binding.classField
	}

	inner class AddCharacterViewHolder(binding: ItemAddCharacterCardBinding) : ViewHolder(binding) {
		init {
			binding.root.setOnClickListener { addListener.onClick() }
		}
	}

	interface OnAddCharacterListener {
		fun onClick() { }
	}

}
