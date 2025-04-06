package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast

class SignupEmailScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_email_screen)

        val FirstName = findViewById<EditText>(R.id.editTextFirstName)
        val LastName = findViewById<EditText>(R.id.editTextLastName)
        val Email = findViewById<EditText>(R.id.editTextEmail)
        val Phone = findViewById<EditText>(R.id.editTextPhone)
        val Password = findViewById<EditText>(R.id.editTextPassword)
        val ConfirmPassword = findViewById<EditText>(R.id.editTextConfirmPassword)

        val backButton: LinearLayout =findViewById(R.id.backButton)
        val buttonSignUp = findViewById<Button>(R.id.buttonSignUp)

        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.editTextConfirmPassword)

        val togglePassword = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirmPassword = findViewById<ImageView>(R.id.toggleConfirmPassword)

        // Set up click listeners for each toggle button
        setupPasswordToggle(togglePassword, passwordField)
        setupPasswordToggle(toggleConfirmPassword, confirmPasswordField)

        backButton.setOnClickListener {
            finish()
        }

        buttonSignUp.setOnClickListener{
            if(FirstName.text.toString().isNullOrEmpty()
                ||LastName.text.toString().isNullOrEmpty()
                ||Email.text.toString().isNullOrEmpty()
                ||Phone.text.toString().isNullOrEmpty()
                ||Password.text.toString().isNullOrEmpty()
                ||ConfirmPassword.text.toString().isNullOrEmpty()){
                    Toast.makeText(this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
            }

            startActivity(
                Intent(this, LoginScreen::class.java).apply{
                    putExtra("FirstName", FirstName.text.toString())
                    putExtra("LastName", LastName.text.toString())
                    putExtra("Email", Email.text.toString())
                    putExtra("Phone", Phone.text.toString())
                    putExtra("Password", Password.text.toString())
                    putExtra("ConfirmPassword", ConfirmPassword.text.toString())
                }
            )
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