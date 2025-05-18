package com.intprog.tableflow
import com.intprog.tableflow.model.User

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import com.intprog.tableflow.api.RetrofitClient
import com.intprog.tableflow.model.SessionManager
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupEmailScreen : Activity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var togglePassword: ImageView
    private lateinit var toggleConfirmPassword: ImageView
    private lateinit var buttonSignUp: Button
    private lateinit var backButton: LinearLayout
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_email_screen)

        // Initialize SessionManager
        sessionManager = SessionManager(this)

        // Check if user is already logged in
        if (sessionManager.checkLogin()) {
            // Redirect to profile screen
            startActivity(Intent(this, ProfileScreen::class.java))
            finish()
        }

        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        togglePassword = findViewById(R.id.togglePassword)
        toggleConfirmPassword = findViewById(R.id.toggleConfirmPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        backButton = findViewById(R.id.backButton)

        val passwordField = findViewById<EditText>(R.id.editTextPassword)
        val confirmPasswordField = findViewById<EditText>(R.id.editTextConfirmPassword)

        val togglePassword = findViewById<ImageView>(R.id.togglePassword)
        val toggleConfirmPassword = findViewById<ImageView>(R.id.toggleConfirmPassword)

        // Set up click listeners for each toggle button
        setupPasswordToggle(togglePassword, passwordField)
        setupPasswordToggle(toggleConfirmPassword, confirmPasswordField)

        // Sign up button click
        buttonSignUp.setOnClickListener {
            signUp()
        }

        // Back button click
        backButton.setOnClickListener {
            val intent = Intent(this, SignupScreen::class.java)
            startActivity(intent)
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

    // Email validation function
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

    private fun signUp() {
        val firstName = editTextFirstName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phone = editTextPhone.text.toString().trim()
        val password = editTextPassword.text.toString().trim()
        val confirmPassword = editTextConfirmPassword.text.toString().trim()

        // Validate inputs
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
            phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate email format
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if email is already registered
        if (sessionManager.isUserRegistered(email)) {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show()
            return
        }

        //retrofit shizzle
        val user = User(firstName, lastName, email, phone, password)
        RetrofitClient.instance.addUser(user).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@SignupEmailScreen, "User added", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@SignupEmailScreen, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("Signup", "onFailure triggered", t)
            }
        })


        // Create user and save to shared preferences
        /*val user = User(firstName, lastName, email, phone, password)*/

        // This will now also call saveUser() internally
        sessionManager.saveUserLoginSession(user)

        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()

        // Navigate to profile screen
        val intent = Intent(this, DashboardScreen::class.java)
        startActivity(intent)
        finish()
    }
}