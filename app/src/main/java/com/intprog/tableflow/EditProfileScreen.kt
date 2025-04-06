package com.intprog.tableflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class EditProfileScreen : Activity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonChangePassword: Button
    private lateinit var buttonUpdate: Button
    private lateinit var backButton: LinearLayout
    private lateinit var changeProfilePicture: TextView
    private lateinit var homeButton: LinearLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.checkLogin()) {
            // Redirect to login screen
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
            return
        }

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextPhone = findViewById(R.id.editTextPhone)
        buttonChangePassword = findViewById(R.id.buttonChangePassword)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        backButton = findViewById(R.id.backButton)
        changeProfilePicture = findViewById(R.id.changeProfilePicture)
        homeButton = findViewById(R.id.homeButton)

        // Load user data
        loadUserData()

        // Update profile button click
        buttonUpdate.setOnClickListener {
            updateProfile()
        }

        // Change password button click
        buttonChangePassword.setOnClickListener {
            val intent = Intent(this, SetNewPassScreen::class.java)
            startActivity(intent)
            finish()
        }

        // Back button click
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Change profile picture click
        changeProfilePicture.setOnClickListener {
            Toast.makeText(this, "Profile picture change feature coming soon", Toast.LENGTH_SHORT).show()
        }

        // Home button (for bottom navigation)
        homeButton.setOnClickListener {
            // Navigate to profile screen
            finish()
        }
    }

    private fun loadUserData() {
        val user = sessionManager.getUserDetails()
        editTextFirstName.setText(user.firstName)
        editTextLastName.setText(user.lastName)
        editTextPhone.setText(user.phone)
    }

    private fun updateProfile() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Update user details
        sessionManager.updateUserDetails(firstName, lastName, phone)
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // Go back to profile screen
        finish()
    }
}