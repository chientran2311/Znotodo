package com.example.znotodo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.znotodo.R
import com.example.znotodo.util.DayPopupHelper
import com.example.znotodo.util.MonthPopupHelper

class StatisticFragment : Fragment() {

    private lateinit var timeRangeDisplay: TextView
    private lateinit var dayDisplay: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val monthHelper = MonthPopupHelper()
        val dayHelper = DayPopupHelper()

        timeRangeDisplay = view.findViewById(R.id.tv_time_range_display)
        dayDisplay = view.findViewById(R.id.tv_day_display)

        // Handle time range popup (month selection)
        timeRangeDisplay.setOnClickListener { anchor ->
            val inflater = LayoutInflater.from(requireContext())
            val popupView = inflater.inflate(R.layout.select_time_range_popup, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            val tvStartMonth = popupView.findViewById<TextView>(R.id.tv_start_month)
            val tvEndMonth = popupView.findViewById<TextView>(R.id.tv_end_month)
            val btnApply = popupView.findViewById<TextView>(R.id.btn_apply)
            val btnCancel = popupView.findViewById<TextView>(R.id.btn_cancel)

            var selectedStart: String? = null
            var selectedEnd: String? = null

            tvStartMonth.setOnClickListener {
                monthHelper.showMonthPopup(requireContext(), tvStartMonth) { selected ->
                    selectedStart = selected
                    tvStartMonth.text = selected
                }
            }

            tvEndMonth.setOnClickListener {
                monthHelper.showMonthPopup(requireContext(), tvEndMonth) { selected ->
                    selectedEnd = selected
                    tvEndMonth.text = selected
                }
            }

            btnApply.setOnClickListener {
                if (!selectedStart.isNullOrEmpty() && !selectedEnd.isNullOrEmpty()) {
                    val startNum = monthToNumber(selectedStart)
                    val endNum = monthToNumber(selectedEnd)
                    timeRangeDisplay.text = "▶ $startNum/25–$endNum/25"
                }
                popupWindow.dismiss()
            }

            btnCancel.setOnClickListener {
                popupWindow.dismiss()
            }

            popupWindow.elevation = 10f
            popupWindow.isFocusable = true
            popupWindow.isOutsideTouchable = true
            popupWindow.showAsDropDown(anchor, 0, 8)
        }

        // Handle day count popup
        dayDisplay.setOnClickListener { anchor ->
            val inflater = LayoutInflater.from(requireContext())
            val popupView = inflater.inflate(R.layout.select_numbers_of_days_popup, null)
            val popupWindow = PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
            )

            val daySelector = popupView.findViewById<TextView>(R.id.tv_day_selector)

            daySelector.setOnClickListener {
                dayHelper.showDayPopup(requireContext(), daySelector) { selectedDay ->
                    val dayText = if (selectedDay == "1") "1 day" else "$selectedDay days"
                    daySelector.text = dayText
                    dayDisplay.text = "▼ In $dayText"
                    popupWindow.dismiss()
                }
            }

            popupWindow.elevation = 10f
            popupWindow.showAsDropDown(anchor, 0, 8)
        }
    }

    private fun monthToNumber(month: String?): String {
        return when (month) {
            "January" -> "1"
            "February" -> "2"
            "March" -> "3"
            "April" -> "4"
            "May" -> "5"
            "June" -> "6"
            "July" -> "7"
            "August" -> "8"
            "September" -> "9"
            "October" -> "10"
            "November" -> "11"
            "December" -> "12"
            else -> ""
        }
    }
}
