package com.intprog.tableflow

import android.app.Activity
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout


class SetNewPassScreen : Activity() {
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
}