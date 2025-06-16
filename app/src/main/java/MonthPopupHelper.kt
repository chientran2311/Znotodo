package com.example.znotodo.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.example.znotodo.R

class MonthPopupHelper {

    fun showMonthPopup(context: Context, anchor: View, onMonthSelected: (String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_month_selector, null)

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        val months = listOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        for ((i, month) in months.withIndex()) {
            val resId = context.resources.getIdentifier("month_${i + 1}", "id", context.packageName)
            val monthView = popupView.findViewById<TextView>(resId)
            monthView?.text = month
            monthView?.setOnClickListener {
                onMonthSelected(month)
                popupWindow.dismiss()
            }
        }

        popupWindow.elevation = 10f
        popupWindow.isOutsideTouchable = true
        popupWindow.isFocusable = true
        popupWindow.showAsDropDown(anchor, 0, 8)
    }
}