package com.example.znotodo

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteConfirmationDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener { dlg ->
            val d = dlg as BottomSheetDialog
            val bottomSheet = d.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.layoutParams?.width = (resources.displayMetrics.widthPixels * 0.85).toInt()
            bottomSheet?.background = null
        }
        return dialog
    }

    private var onDeleteConfirmed: (() -> Unit)? = null

    fun setOnDeleteConfirmedListener(listener: () -> Unit) {
        onDeleteConfirmed = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.single_delete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<Button>(R.id.btn_no)
        val btnDelete = view.findViewById<Button>(R.id.btn_yes)

        btnCancel.setOnClickListener {
            dismiss()
        }

        btnDelete.setOnClickListener {
            onDeleteConfirmed?.invoke()
            dismiss()
        }
    }


}