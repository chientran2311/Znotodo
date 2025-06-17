package com.example.znotodo.fragment


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.znotodo.CreateNotesActivity
import com.example.znotodo.EditNotesActivity
import com.example.znotodo.R

class NotesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.notes, container, false)

        val moreOptionsButton = view.findViewById<ImageView>(R.id.imageView2)
        moreOptionsButton.setOnClickListener {
            showPopupMenu(it)
        }
        val bigPlusButton = view.findViewById<ImageView>(R.id.big_plus)
        bigPlusButton.setOnClickListener {
            val intent = Intent(requireContext(), CreateNotesActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        val noteList = view.findViewById<LinearLayout>(R.id.noteList)

        for (i in 0 until noteList.childCount) {
            val noteItem = noteList.getChildAt(i)

            noteItem.setOnClickListener {
                // Optional: Lấy title, content từ trong view con nếu có
                val noteTitleView = noteItem.findViewById<TextView>(R.id.noteTitle)
                val noteContentView = noteItem.findViewById<TextView>(R.id.noteContent)

                val title = noteTitleView?.text?.toString() ?: "Note#${i + 1}"
                val content = noteContentView?.text?.toString() ?: ""

                val intent = Intent(requireContext(), EditNotesActivity::class.java).apply {
                    putExtra("noteTitle", title)
                    putExtra("noteContent", content)
                }

                startActivity(intent)
                requireActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }

        return view
    }

    private fun showPopupMenu(anchorView: View) {
        val popup = PopupMenu(requireContext(), anchorView)
        popup.menuInflater.inflate(R.menu.edit_note_menu, popup.menu)

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_edit -> {
                    val selectItemFragment = SelectItemFragment()

                    parentFragmentManager.beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment_container, selectItemFragment)
                        .addToBackStack(null) // để khi nhấn back quay lại NotesFragment
                        .commit()

                    true
                }
                R.id.menu_settings -> {
                    Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}
