package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val forgotPasswordLink = findViewById<TextView>(R.id.forgotPasswordLink)
        val fb_signinbtn: RelativeLayout = findViewById(R.id.fb_signinbtn)
        val google_signinbtn: RelativeLayout = findViewById(R.id.google_signinbtn)
        val backButton: LinearLayout = findViewById(R.id.backButton)
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)

        backButton.setOnClickListener {
            finish()
        }

        buttonSignIn.setOnClickListener{
            val intent = Intent(this,DashboardScreen::class.java)
            startActivity(intent)
        }

        forgotPasswordLink.setOnClickListener {
            val intent = Intent(this,ForgotPassScreen::class.java)
            startActivity(intent)
        }

        fb_signinbtn.setOnClickListener{
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        google_signinbtn.setOnClickListener{
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }
    }
}