package com.example.znotodo.util

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.example.znotodo.R

class DayPopupHelper {

    fun showDayPopup(context: Context, anchor: View, onDaySelected: (String) -> Unit) {
        val inflater = LayoutInflater.from(context)
        val popupView = inflater.inflate(R.layout.popup_day_selector, null)
        val popupWindow = PopupWindow(
            popupView,
            anchor.width,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Duyệt qua các TextView từ day_1 đến day_30 trong popup_day_selector.xml
        for (i in 1..30) {
            val resId = context.resources.getIdentifier("day_$i", "id", context.packageName)
            val dayView = popupView.findViewById<TextView>(resId)
            val dayText = if (i == 1) "$i day" else "$i days"

            dayView?.text = dayText
            dayView?.setOnClickListener {
                // Trả về text được chọn qua callback
                onDaySelected(i.toString())
                popupWindow.dismiss()
            }
        }

        popupWindow.elevation = 10f
        popupWindow.showAsDropDown(anchor, 0, 8)
    }
}