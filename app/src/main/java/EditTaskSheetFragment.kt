package com.example.znotodo

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditTaskSheetFragment : BottomSheetDialogFragment() {
    private lateinit var bellButton: ImageButton
    private var reminderTextView: TextView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_task_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bellButton = view.findViewById<ImageButton>(R.id.imageButton2)

        bellButton.setOnClickListener {
            // Show SetScheduelFragment as a dialog with callback
            val scheduleFragment = SetScheduelFragment()
            scheduleFragment.setOnSaveClickListener { reminderText ->
                // Replace bell icon with reminder layout
                bellButton.visibility = View.GONE

                // Create or get reminder container
                if (reminderTextView == null) {
                    // Create a horizontal linear layout to hold the components
                    val reminderLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL
                        background = resources.getDrawable(R.drawable.reminder_background)
                        setPadding(16, 8, 16, 8)

                        // Add small bell icon on the left
                        val bellIcon = ImageView(context).apply {
                            setImageResource(R.drawable.bell)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                marginEnd = 8
                            }
                        }
                        addView(bellIcon)

                        // Add text in the middle
                        reminderTextView = TextView(context).apply {
                            text = reminderText
                            textSize = 14f
                            setTextColor(resources.getColor(android.R.color.black))
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                weight = 1f
                            }
                        }
                        addView(reminderTextView)

                        // Add X button on the right
                        val closeButton = ImageView(context).apply {
                            setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                marginStart = 8
                            }
                            setOnClickListener {
                                // Remove the reminder layout and show bell icon again
                                (parent as? ViewGroup)?.removeView(this@apply.parent as View)
                                bellButton.visibility = View.VISIBLE
                            }
                        }
                        addView(closeButton)
                    }

                    // Add to the parent container
                    val parent = bellButton.parent as ViewGroup
                    val params = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    parent.addView(reminderLayout, params)

                    // Position like the bell button
                    reminderLayout.x = bellButton.x
                    reminderLayout.y = bellButton.y
                } else {
                    reminderTextView?.text = reminderText
                }
            }
            scheduleFragment.show(parentFragmentManager, "SetScheduel")
        }
    }
}