package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast


class EditProfileScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)

        val backButton : LinearLayout = findViewById(R.id.backButton)
        val buttonUpdate = findViewById<Button>(R.id.buttonUpdate)
        val buttonChangePassword = findViewById<Button>(R.id.buttonChangePassword)


        backButton.setOnClickListener{
            finish()
        }
        buttonUpdate.setOnClickListener {
            Toast.makeText(this,"Profile Updated Successfully!", Toast.LENGTH_LONG).show()
            val intent = Intent(this,ProfileScreen::class.java)
            startActivity(intent)
        }
        buttonChangePassword.setOnClickListener{
            val intent = Intent(this,SetNewPassScreen::class.java)
            startActivity(intent)
        }

        //navbar
        val homeButton : LinearLayout = findViewById(R.id.homeButton)

        homeButton.setOnClickListener {
            val intent = Intent(this,DashboardScreen::class.java)
            startActivity(intent)
        }

    }
}