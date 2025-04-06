package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfileScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        val backButton : LinearLayout = findViewById(R.id.backButton)

        editProfileButton.setOnClickListener {
            val intent = Intent(this,EditProfileScreen::class.java)
            startActivity(intent)
        }
        backButton.setOnClickListener{
            finish()
        }

        //navbar
        val homeButton : LinearLayout = findViewById(R.id.homeButton)
        homeButton.setOnClickListener {
            val intent = Intent(this,DashboardScreen::class.java)
            startActivity(intent)
        }
    }
}