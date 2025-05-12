package com.intprog.tableflow

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ImageView
import com.intprog.tableflow.model.SessionManager

class EditProfileScreen : Activity() {

    private var originalFirstName: String = ""
    private var originalLastName: String = ""
    private var originalPhone: String = ""
    private var originalProfilePictureId: Int = -1
    private var temporaryProfilePictureId: Int = -1
    private val PROFILE_PICTURE_REQUEST_CODE = 1001

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
        val buttonUpdate: Button = findViewById(R.id.buttonUpdate)
        val backButton: ImageView = findViewById(R.id.backButton)
        val changeProfilePicture: TextView = findViewById(R.id.changeProfilePicture)
        val profileChange: FrameLayout = findViewById(R.id.profileChange)
        val profileImage: ImageView = findViewById(R.id.profileImage)
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notificationButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)
        val moreButton: LinearLayout = findViewById(R.id.moreButton)

        loadUserData(sessionManager, editTextFirstName, editTextLastName, editTextPhone, profileImage)

        buttonUpdate.setOnClickListener {
            if (isAnyChangesMade()) {
                showConfirmationDialog(sessionManager, editTextFirstName, editTextLastName, editTextPhone)
            } else {
                Toast.makeText(this, "No changes made to profile", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        backButton.setOnClickListener {
            if (isAnyChangesMade()) {
                showUnsavedChangesDialog()
            } else {
                finish()
            }
        }

        profileChange.setOnClickListener {
            val intent = Intent(this, ProfilePictureSelectionScreen::class.java)
            startActivityForResult(intent, PROFILE_PICTURE_REQUEST_CODE)
        }

        changeProfilePicture.setOnClickListener {
            val intent = Intent(this, ProfilePictureSelectionScreen::class.java)
            startActivityForResult(intent, PROFILE_PICTURE_REQUEST_CODE)
        }

        homeButton.setOnClickListener {
            if (isAnyChangesMade()) {
                showNavigationDialog(Intent(this, DashboardScreen::class.java))
            } else {
                startActivity(Intent(this, DashboardScreen::class.java))
            }
        }

        notificationButton.setOnClickListener {
            if (isAnyChangesMade()) {
                showNavigationDialog(Intent(this, NotificationScreen::class.java))
            } else {
                startActivity(Intent(this, NotificationScreen::class.java))
            }
        }

        moreButton.setOnClickListener {
            if (isAnyChangesMade()) {
                showNavigationDialog(Intent(this, ProfileScreen::class.java))
            } else {
                startActivity(Intent(this, ProfileScreen::class.java))
            }
        }

        historyButton.setOnClickListener {
            if (isAnyChangesMade()) {
                showNavigationDialog(Intent(this, HistoryScreen::class.java))
            } else {
                startActivity(Intent(this, HistoryScreen::class.java))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PROFILE_PICTURE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Store the selected profile picture ID temporarily
            data?.let {
                temporaryProfilePictureId = it.getIntExtra("SELECTED_PROFILE_PICTURE_ID", originalProfilePictureId)

                // Update profile image preview with the temporary selection
                val profileImage: ImageView = findViewById(R.id.profileImage)
                profileImage.setImageResource(temporaryProfilePictureId)
            }
        }
    }

    private fun loadUserData(sessionManager: SessionManager,
                             editTextFirstName: EditText,
                             editTextLastName: EditText,
                             editTextPhone: EditText,
                             profileImage: ImageView) {
        val user = sessionManager.getUserDetails()

        // Store original values
        originalFirstName = user.firstName
        originalLastName = user.lastName
        originalPhone = user.phone
        originalProfilePictureId = user.profilePictureId

        // Initialize temporary profile picture with current one
        temporaryProfilePictureId = originalProfilePictureId

        // Set values to EditText fields
        editTextFirstName.setText(originalFirstName)
        editTextLastName.setText(originalLastName)
        editTextPhone.setText(originalPhone)

        // Set profile picture
        profileImage.setImageResource(originalProfilePictureId)
    }

    private fun isAnyChangesMade(): Boolean {
        val editTextFirstName: EditText = findViewById(R.id.editTextFirstName)
        val editTextLastName: EditText = findViewById(R.id.editTextLastName)
        val editTextPhone: EditText = findViewById(R.id.editTextPhone)

        val currentFirstName = editTextFirstName.text.toString().trim()
        val currentLastName = editTextLastName.text.toString().trim()
        val currentPhone = editTextPhone.text.toString().trim()

        return currentFirstName != originalFirstName ||
                currentLastName != originalLastName ||
                currentPhone != originalPhone ||
                temporaryProfilePictureId != originalProfilePictureId
    }

    private fun isProfileTextChanged(editTextFirstName: EditText,
                                     editTextLastName: EditText,
                                     editTextPhone: EditText): Boolean {
        val currentFirstName = editTextFirstName.text.toString().trim()
        val currentLastName = editTextLastName.text.toString().trim()
        val currentPhone = editTextPhone.text.toString().trim()

        return currentFirstName != originalFirstName ||
                currentLastName != originalLastName ||
                currentPhone != originalPhone
    }

    private fun showUnsavedChangesDialog() {
        AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("You have unsaved changes. Do you want to discard them?")
            .setPositiveButton("Discard") { _, _ ->
                finish()
            }
            .setNegativeButton("Keep Editing", null)
            .show()
    }

    private fun showNavigationDialog(intent: Intent) {
        AlertDialog.Builder(this)
            .setTitle("Unsaved Changes")
            .setMessage("You have unsaved changes. Do you want to discard them and navigate away?")
            .setPositiveButton("Discard and Navigate") { _, _ ->
                startActivity(intent)
            }
            .setNegativeButton("Keep Editing", null)
            .show()
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

        // Update user details including the profile picture
        sessionManager.updateUserDetails(firstName, lastName, phone)

        // Update profile picture if changed
        if (temporaryProfilePictureId != originalProfilePictureId) {
            sessionManager.updateProfilePicture(temporaryProfilePictureId)
        }

        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

        // Go back to profile screen
        finish()
    }

    // Handle system back button press
    override fun onBackPressed() {
        if (isAnyChangesMade()) {
            showUnsavedChangesDialog()
        } else {
            super.onBackPressed()
        }
    }
}