package com.intprog.tableflow

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ImageView
import android.widget.Toast
import android.text.method.PasswordTransformationMethod
import com.intprog.tableflow.model.SessionManager


class SetNewPassScreen : Activity() {

    private lateinit var editTextCurrentPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonChangePassword: Button
    private lateinit var backButton: LinearLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_pass_screen)

        // Initialize views
        val currentPasswordField = findViewById<EditText>(R.id.editTextCurrentPassword)
        val newPasswordField = findViewById<EditText>(R.id.editTextNewPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.editTextConfirmPassword)

        val toggleCurrentPassword = findViewById<ImageView>(R.id.toggleCurrentPassword)
        val toggleNewPassword = findViewById<ImageView>(R.id.toggleNewPassword)
        val toggleConfirmPassword = findViewById<ImageView>(R.id.toggleConfirmPassword)

        // Set up click listeners for each toggle button
        setupPasswordToggle(toggleCurrentPassword, currentPasswordField)
        setupPasswordToggle(toggleNewPassword, newPasswordField)
        setupPasswordToggle(toggleConfirmPassword, confirmPasswordField)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Check if user is logged in
        if (!sessionManager.checkLogin()) {
            finish()
            return
        }

        // Initialize views
        editTextCurrentPassword = findViewById(R.id.editTextCurrentPassword)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        buttonChangePassword = findViewById(R.id.buttonChangePassword)
        backButton = findViewById(R.id.backButton)

        // Change password button click
        buttonChangePassword.setOnClickListener {
            changePassword()
        }

        val backButton: LinearLayout =findViewById(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }
    }

    private fun setupPasswordToggle(toggleButton: ImageView, passwordField: EditText) {
        var isPasswordVisible = false

        toggleButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible

            // Change password visibility
            if (isPasswordVisible) {
                // Show password - set to null to show as plain text
                passwordField.transformationMethod = null
                toggleButton.setImageResource(R.drawable.eye_slash_solid) // Assuming you have this icon
            } else {
                // Hide password
                passwordField.transformationMethod = PasswordTransformationMethod.getInstance()
                toggleButton.setImageResource(R.drawable.eye_solid)
            }

            // Maintain cursor position
            passwordField.setSelection(passwordField.text.length)
        }
    }

    private fun changePassword() {
        val currentPwd = editTextCurrentPassword.text.toString().trim()
        val newPwd = editTextNewPassword.text.toString().trim()
        val confirmPwd = editTextConfirmPassword.text.toString().trim()

        // Get current user details
        val user = sessionManager.getUserDetails()

        if (currentPwd.isEmpty() || newPwd.isEmpty() || confirmPwd.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else if (currentPwd != user.password) {
            Toast.makeText(this, "Current password is incorrect", Toast.LENGTH_SHORT).show()
        } else if (newPwd != confirmPwd) {
            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
        } else if (newPwd.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        } else {
            // Update password
            sessionManager.updatePassword(newPwd)
            Toast.makeText(this, "Password changed successfully!", Toast.LENGTH_SHORT).show()

            // Return to previous screen
            finish()
        }
    }
}