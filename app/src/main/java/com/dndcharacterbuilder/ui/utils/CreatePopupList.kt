package com.dndcharacterbuilder.ui

import android.content.Context
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import com.dndcharacterbuilder.R

fun createPopupList (field: TextView, items: List<String>, context: Context): ListPopupWindow {
    if (items.isEmpty()) {
        field.visibility = View.INVISIBLE
    } else {
        field.visibility = View.VISIBLE
    }
    val popup = ListPopupWindow(context).apply {
        setAdapter(ArrayAdapter(context, R.layout.item_popup, items))
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
    return popup
}