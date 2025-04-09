package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.RelativeLayout
import com.intprog.tableflow.model.SessionManager

class LoginScreen : Activity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var forgotPasswordLink: TextView
    private lateinit var backButton: LinearLayout
    private lateinit var fbSignInBtn: RelativeLayout
    private lateinit var googleSignInBtn: RelativeLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.checkLogin()) {
            // Redirect to dashboard screen
            startActivity(Intent(this, DashboardScreen::class.java))
            finish()
        }

        // Initialize views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink)
        backButton = findViewById(R.id.backButton)
        fbSignInBtn = findViewById(R.id.fb_signinbtn)
        googleSignInBtn = findViewById(R.id.google_signinbtn)

        // Sign in button click
        buttonSignIn.setOnClickListener {
            signIn()
        }

        // Forgot password click
        forgotPasswordLink.setOnClickListener {
            val intent = Intent(this, ForgotPassScreen::class.java)
            startActivity(intent)
            finish()
        }

        // Back button click
        backButton.setOnClickListener {
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
            finish()
        }

        // Social sign in buttons (placeholders for now)
        fbSignInBtn.setOnClickListener {
            Toast.makeText(this, "Facebook sign in coming soon", Toast.LENGTH_SHORT).show()
        }

        googleSignInBtn.setOnClickListener {
            Toast.makeText(this, "Google sign in coming soon", Toast.LENGTH_SHORT).show()
        }
    }

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun signIn() {
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        // Validate inputs
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate credentials
        if (sessionManager.validateUser(email, password)) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

            // Navigate to dashboard screen
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }
}