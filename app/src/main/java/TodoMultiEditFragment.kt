import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.znotodo.DeleteConfirmationDialogFragment
import com.example.znotodo.MainActivity
import com.example.znotodo.R
import com.google.android.material.button.MaterialButton

class TodoMultiEditFragment : Fragment(R.layout.todo_edit_multi) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? MainActivity)?.setBottomNavVisible(false)

        val cancelButton = view.findViewById<TextView>(R.id.cancelBtn)
        cancelButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        

        val deleteButton = view.findViewById<MaterialButton>(R.id.btnDeleteTodos)
        deleteButton.setOnClickListener {
            val confirmDialog = DeleteConfirmationDialogFragment()
            confirmDialog.setOnDeleteConfirmedListener {
                // ✅ TODO: xử lý xóa to-dos ở đây nếu có

                // ✅ Quay lại màn trước đó
                parentFragmentManager.popBackStack()
            }
            confirmDialog.show(parentFragmentManager, "DeleteConfirmationDialog")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as? MainActivity)?.setBottomNavVisible(true)
    }
}