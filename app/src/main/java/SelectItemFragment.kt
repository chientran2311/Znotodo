package com.example.znotodo.fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.os.Bundle
import android.transition.Fade
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.znotodo.MainActivity
import com.example.znotodo.R
import androidx.appcompat.app.AlertDialog
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog

class SelectItemFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Áp dụng hiệu ứng dissolve (fade transition)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Ẩn BottomNavigationView của MainActivity
        (activity as? MainActivity)?.setBottomNavVisibility(false)

        return inflater.inflate(R.layout.select_item, container, false)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setBottomNavVisibility(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bắt sự kiện cho bottom navigation
        val bottomNavSelect = view.findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavSelect.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_statistic -> {
                    showDeleteDialog()
                    true
                }
                else -> false
            }
        }

        // Bắt sự kiện cho nút "Cancel" (TextView)
        val cancelText = view.findViewById<TextView>(R.id.textView_cancel)
        cancelText.setOnClickListener {
            // Quay lại NotesFragment khi bấm Cancel
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out) // hiệu ứng dissolve
                .replace(R.id.fragment_container, NotesFragment())
                .commit()
        }
    }
    private fun showDeleteDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_delete_confirm, null)
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)

        bottomSheetDialog.setContentView(dialogView)

        // Bo góc nếu cần (nếu bạn đã setup background drawable bo tròn)
        bottomSheetDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val cancelBtn = dialogView.findViewById<TextView>(R.id.btn_cancel)
        val deleteBtn = dialogView.findViewById<TextView>(R.id.btn_delete)

        cancelBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        deleteBtn.setOnClickListener {
            // TODO: Xử lý xóa các item đã chọn
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }



}
