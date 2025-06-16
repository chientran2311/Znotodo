package com.example.znotodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewTaskSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_task_sheet, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bellButton = view.findViewById<ImageButton>(R.id.imageButton2)
        bellButton.setOnClickListener {
            // Show SetScheduelFragment as a dialog
            SetScheduelFragment().show(parentFragmentManager, "SetScheduel")
        }
    }
}