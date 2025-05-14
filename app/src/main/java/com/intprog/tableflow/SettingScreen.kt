package com.intprog.tableflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.SessionManager

class SettingScreen : Activity() {
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting_screen)

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
        val profileSection: RelativeLayout = findViewById(R.id.profileSection)
        val editProfileButton: LinearLayout = findViewById(R.id.editProfileButton)
        val changePasswordButton: LinearLayout = findViewById(R.id.changePasswordButton)
        val aboutAppButton: LinearLayout = findViewById(R.id.aboutAppButton)
        val devTeamButton: LinearLayout = findViewById(R.id.devTeamButton)
        val logoutButton: LinearLayout = findViewById(R.id.logoutButton)
        val backButton: ImageView = findViewById(R.id.backButton)

        // Navigation bar buttons
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notificationButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)
        val moreButton: LinearLayout = findViewById(R.id.moreButton)

        // Load user data on startup
        loadUserData()

        // Set click listeners for all buttons
        profileSection.setOnClickListener {
            startActivity(Intent(this, ProfileScreen::class.java))
        }

        editProfileButton.setOnClickListener {
            startActivity(Intent(this, EditProfileScreen::class.java))
        }

        changePasswordButton.setOnClickListener {
            startActivity(Intent(this, SetNewPassScreen::class.java))
        }

        aboutAppButton.setOnClickListener {
            startActivity(Intent(this, AboutScreen::class.java))
        }

        devTeamButton.setOnClickListener {
            startActivity(Intent(this, DevelopersScreen::class.java))
        }

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog()
        }

        //added back button I'm a genius
        //⣿⠿⠿⠛⠛⠛⠟⠿⠻⣿⣿⡿⠿⠿⠛⠛⠛⠛⠿⢿
        //⣷⣴⣾⠿⠿⠿⣷⢶⢾⣿⣿⡿⢶⡶⠿⠻⠛⣿⣶⣽
        //⣿⣿⣏⠶⠀⠀⠭⢺⣺⣿⣿⣧⣺⠧⠄⠀⡰⢛⣽⣿
        //⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿
        //⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿
        //⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⢏⣾⣿
        //⣿⣿⣿⣿⣿⠻⠿⠿⠿⠿⠿⠿⢛⣛⣭⣾⣿⣸⣿⣿
        //⣿⣿⣿⣿⣿⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⣿⣿⣿
        backButton.setOnClickListener {
            // For now, just finish the activity
            finish()
        }

        // Navigation bar listeners
        homeButton.setOnClickListener {
            startActivity(Intent(this, DashboardScreen::class.java))
        }

        notificationButton.setOnClickListener {
            startActivity(Intent(this, NotificationScreen::class.java))
        }

        historyButton.setOnClickListener {
            startActivity(Intent(this, HistoryScreen::class.java))
        }

        // We're already on the More/Settings screen, so no action needed
        moreButton.setOnClickListener {
            // Already on settings screen
        }
    }

    override fun onResume() {
        super.onResume()
        // Reload user data when returning to this screen
        loadUserData()
    }

    private fun loadUserData() {
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val textViewFullName: TextView = findViewById(R.id.textViewFullName)
        val textViewEmail: TextView = findViewById(R.id.textViewEmail)

        val user = sessionManager.getUserDetails()
        textViewFullName.text = "${user.firstName} ${user.lastName}"
        textViewEmail.text = user.email
        profileImage.setImageResource(user.profilePictureId)
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