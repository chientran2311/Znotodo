package com.example.znotodo  // đúng tên package của app bạn

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class CreateNotesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.create_note)  // hoặc đúng tên layout của bạn
        val backBtn = findViewById<ImageView>(R.id.btnBack)
        backBtn.setOnClickListener {
            finish() // đóng activity, trở về notes fragment
        }
    }
}
