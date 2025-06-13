package com.example.znotodo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.notes) // hoặc activity_notes nếu layout của bạn tên vậy
    }
}
