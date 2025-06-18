package com.example.znotodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment

class SetScheduelFragment : DialogFragment() {

    private var onSaveClickListener: ((String) -> Unit)? = null

    fun setOnSaveClickListener(listener: (String) -> Unit) {
        onSaveClickListener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_set_scheduel, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the cancel button and set click listener
        val cancelButton = view.findViewById<TextView>(R.id.btn_cancel)
        cancelButton.setOnClickListener {
            // Dismiss this dialog fragment
            dismiss()
        }

        // Find save button and set click listener
        val saveButton = view.findViewById<TextView>(R.id.btn_save)
        saveButton.setOnClickListener {
            // Call the callback with reminder text
            onSaveClickListener?.invoke("Today, 10:30")
            dismiss()
        }
    }
}