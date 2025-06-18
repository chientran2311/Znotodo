package com.example.znotodo

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.znotodo.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        bottomNav = findViewById(R.id.bottom_navigation)
        loadFragment(NotesFragment())

        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_notes -> loadFragment(NotesFragment())
                R.id.nav_todo -> loadFragment(TodoFragment())
                R.id.nav_static -> loadFragment(StatisticFragment())
            }
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right, R.anim.slide_out_left
            )
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun setBottomNavVisibility(visible: Boolean) {
        bottomNav.visibility = if (visible) View.VISIBLE else View.GONE
    }


}
