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

class ForgotPassScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass_screen)

        val backButton: LinearLayout =findViewById(R.id.backButton)
        val buttonResetPassword = findViewById<Button>(R.id.buttonResetPassword)

        backButton.setOnClickListener {
            finish()
        }

//        I don't think this is the right page lol idk sa prototype
        buttonResetPassword.setOnClickListener{
            val intent = Intent(this,SetNewPassScreen::class.java)
            startActivity(intent)
        }

    }
}