package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignupScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_screen)

        val email_signupbtn = findViewById<Button>(R.id.email_signupbtn)
        val fb_signupbtn: RelativeLayout =findViewById(R.id.fb_signupbtn)
        val google_signupbtn: RelativeLayout =findViewById(R.id.google_signupbtn)
        val signin_btn = findViewById<TextView>(R.id.signin_btn)

        email_signupbtn.setOnClickListener {
            val intent = Intent(this,SignupEmailScreen::class.java)
            startActivity(intent)
        }
        fb_signupbtn.setOnClickListener{
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }
        google_signupbtn.setOnClickListener{
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }
        signin_btn.setOnClickListener{
            val intent = Intent(this,LoginScreen::class.java)
            startActivity(intent)
        }
    }
}