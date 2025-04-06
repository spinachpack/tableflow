package com.intprog.tableflow

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginScreen : Activity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)

        val forgotPasswordLink = findViewById<TextView>(R.id.forgotPasswordLink)
        val fb_signinbtn: RelativeLayout = findViewById(R.id.fb_signinbtn)
        val google_signinbtn: RelativeLayout = findViewById(R.id.google_signinbtn)
        val backButton: LinearLayout = findViewById(R.id.backButton)
        val buttonSignIn = findViewById<Button>(R.id.buttonSignIn)

        intent?.let{
            it.getStringExtra("Email")?.let{ Email ->
                editTextEmail.setText(Email)
            }
            it.getStringExtra("Password")?.let{ Password ->
                editTextPassword.setText(Password)
            }
        }

        backButton.setOnClickListener {
            finish()
        }

        buttonSignIn.setOnClickListener{
            if(editTextEmail.text.toString().isNullOrEmpty()
                ||editTextPassword.text.toString().isNullOrEmpty()){
                Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else if(editTextEmail.text.toString() != intent.getStringExtra("Email")
                || editTextPassword.text.toString() != intent.getStringExtra("Password")){
                Toast.makeText(this, "Invalid credentials!.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            startActivity(
                Intent(this, DashboardScreen::class.java).apply{
                    putExtra("Email", editTextEmail.text.toString())
                    putExtra("Password", editTextPassword.text.toString())
                }
            )
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