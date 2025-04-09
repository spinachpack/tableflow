package com.intprog.tableflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.SessionManager

class EditProfileScreen : Activity() {

    private var originalFirstName: String = ""
    private var originalLastName: String = ""
    private var originalPhone: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)

        val sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.checkLogin()) {
            // Redirect to login screen
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
            return
        }

        val editTextFirstName: EditText = findViewById(R.id.editTextFirstName)
        val editTextLastName: EditText = findViewById(R.id.editTextLastName)
        val editTextPhone: EditText = findViewById(R.id.editTextPhone)
        val buttonChangePassword: Button = findViewById(R.id.buttonChangePassword)
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val backButton: LinearLayout = findViewById(R.id.backButton)
        val changeProfilePicture: TextView = findViewById(R.id.changeProfilePicture)
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notificationButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)
        val moreButton: LinearLayout = findViewById(R.id.moreButton)

        loadUserData(sessionManager, editTextFirstName, editTextLastName, editTextPhone)

        buttonUpdate.setOnClickListener {
            if (isProfileChanged(editTextFirstName, editTextLastName, editTextPhone)) {
                showConfirmationDialog(sessionManager, editTextFirstName, editTextLastName, editTextPhone)
            } else {
                Toast.makeText(this, "No changes made to profile", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        buttonChangePassword.setOnClickListener {
            val intent = Intent(this, SetNewPassScreen::class.java)
            startActivity(intent)
            finish()
        }

        backButton.setOnClickListener {
            finish()
        }

        changeProfilePicture.setOnClickListener {
            Toast.makeText(this, "Profile picture change feature coming soon", Toast.LENGTH_SHORT).show()
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
        }
        notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationScreen::class.java)
            startActivity(intent)
        }
        moreButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }
    }

    private fun loadUserData(sessionManager: SessionManager,
                             editTextFirstName: EditText,
                             editTextLastName: EditText,
                             editTextPhone: EditText) {
        val user = sessionManager.getUserDetails()

        // Store original values
        originalFirstName = user.firstName
        originalLastName = user.lastName
        originalPhone = user.phone

        // Set values to EditText fields
        editTextFirstName.setText(originalFirstName)
        editTextLastName.setText(originalLastName)
        editTextPhone.setText(originalPhone)
    }

    private fun isProfileChanged(editTextFirstName: EditText,
                                 editTextLastName: EditText,
                                 editTextPhone: EditText): Boolean {
        val currentFirstName = editTextFirstName.text.toString().trim()
        val currentLastName = editTextLastName.text.toString().trim()
        val currentPhone = editTextPhone.text.toString().trim()

        return currentFirstName != originalFirstName ||
                currentLastName != originalLastName ||
                currentPhone != originalPhone
    }

    private fun showConfirmationDialog(sessionManager: SessionManager,
                                       editTextFirstName: EditText,
                                       editTextLastName: EditText,
                                       editTextPhone: EditText) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Update")
            .setMessage("Are you sure you want to update your profile information?")
            .setPositiveButton("Yes") { _, _ ->
                updateProfile(sessionManager, editTextFirstName, editTextLastName, editTextPhone)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun updateProfile(sessionManager: SessionManager,
                              editTextFirstName: EditText,
                              editTextLastName: EditText,
                              editTextPhone: EditText) {
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