package com.example.znotodo

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class EditNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.edit_notes)
        val backBtn = findViewById<ImageView>(R.id.btnBack)
        backBtn.setOnClickListener {
            finish() // đóng activity, trở về notes fragment
        }
        // Nếu cần truyền dữ liệu từ MainActivity, xử lý tại đây
        val noteTitle = intent.getStringExtra("noteTitle")
        val noteContent = intent.getStringExtra("noteContent")

        // Hiển thị dữ liệu trong EditText
        val titleEditText = findViewById<EditText>(R.id.etNoteTitle)
        val contentEditText = findViewById<EditText>(R.id.etNoteContent)

        titleEditText.setText(noteTitle)
        contentEditText.setText(noteContent)

        contentEditText.requestFocus()

        val imm = getSystemService(INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
        imm.showSoftInput(contentEditText, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)

    }
}
