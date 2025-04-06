package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.app.AlertDialog

class ProfileScreen : Activity() {

    private lateinit var textViewFullName: TextView
    private lateinit var textViewPhone: TextView
    private lateinit var textViewEmail: TextView
    private lateinit var editProfileButton: Button
    private lateinit var logoutButton: Button
    private lateinit var backButton: LinearLayout
    private lateinit var homeButton: LinearLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

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
        textViewFullName = findViewById(R.id.textViewFullName)
        textViewPhone = findViewById(R.id.textViewPhone)
        textViewEmail = findViewById(R.id.textViewEmail)
        editProfileButton = findViewById(R.id.editProfileButton)
        logoutButton = findViewById(R.id.logoutButton)
        backButton = findViewById(R.id.backButton)
        homeButton = findViewById(R.id.homeButton)

        // Load user data
        loadUserData()

        // Edit profile button click
        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileScreen::class.java)
            startActivity(intent)
        }

        // Logout button click
        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        // Back button (we can make it go to a home screen or dashboard later)
        backButton.setOnClickListener {
            // For now, just show a toast
            finish()
        }

        // Home button (for bottom navigation)
        homeButton.setOnClickListener {
            // You can implement home screen navigation here
            // For now, we'll just refresh the current screen
            loadUserData()
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload user data when returning to this screen
        loadUserData()
    }

    private fun loadUserData() {
        val user = sessionManager.getUserDetails()
        textViewFullName.text = "${user.firstName} ${user.lastName}"
        textViewPhone.text = user.phone
        textViewEmail.text = user.email
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Perform logout
                sessionManager.logout()

                // Navigate to login screen
                val intent = Intent(this, LoginScreen::class.java)
                // Clear all previous activities
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}