package com.intprog.tableflow
import com.intprog.tableflow.model.User
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        private const val IS_LOGGED_IN = "IsLoggedIn"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_EMAIL = "email"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_REGISTERED_EMAILS = "all_emails"
    }

    fun saveUserLoginSession(user: User) {
        editor.putBoolean(IS_LOGGED_IN, true)
        editor.putString(KEY_FIRST_NAME, user.firstName)
        editor.putString(KEY_LAST_NAME, user.lastName)
        editor.putString(KEY_EMAIL, user.email)
        editor.putString(KEY_PHONE, user.phone)
        editor.putString(KEY_PASSWORD, user.password)
        editor.apply()

        // Also save user to the user registry
        saveUser(user)
    }

    fun saveUser(user: User) {
        val gson = Gson()
        val userJson = gson.toJson(user)

        // Save individual user under their email
        editor.putString("user_${user.email}", userJson)

        // Also mark them as registered
        val allEmails = sharedPreferences.getStringSet(KEY_REGISTERED_EMAILS, HashSet()) ?: HashSet()
        val updatedEmails = HashSet(allEmails)
        updatedEmails.add(user.email)
        editor.putStringSet(KEY_REGISTERED_EMAILS, updatedEmails)

        editor.apply()
    }

    fun updateUserDetails(firstName: String, lastName: String, phone: String) {
        // Update the current session
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.putString(KEY_LAST_NAME, lastName)
        editor.putString(KEY_PHONE, phone)
        editor.apply()

        // Also update the stored user record
        val email = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
        val password = sharedPreferences.getString(KEY_PASSWORD, "") ?: ""

        if (email.isNotEmpty()) {
            val user = User(firstName, lastName, email, phone, password)
            saveUser(user)
        }
    }

    fun updatePassword(newPassword: String) {
        // Update the current session
        editor.putString(KEY_PASSWORD, newPassword)
        editor.apply()

        // Also update the stored user record
        val email = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
        if (email.isNotEmpty()) {
            val firstName = sharedPreferences.getString(KEY_FIRST_NAME, "") ?: ""
            val lastName = sharedPreferences.getString(KEY_LAST_NAME, "") ?: ""
            val phone = sharedPreferences.getString(KEY_PHONE, "") ?: ""

            val user = User(firstName, lastName, email, phone, newPassword)
            saveUser(user)
        }
    }

    fun getUserDetails(): User {
        return User(
            firstName = sharedPreferences.getString(KEY_FIRST_NAME, "") ?: "",
            lastName = sharedPreferences.getString(KEY_LAST_NAME, "") ?: "",
            email = sharedPreferences.getString(KEY_EMAIL, "") ?: "",
            phone = sharedPreferences.getString(KEY_PHONE, "") ?: "",
            password = sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
        )
    }

    fun getUserByEmail(email: String): User? {
        val userJson = sharedPreferences.getString("user_${email}", null) ?: return null
        return try {
            Gson().fromJson(userJson, User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun checkLogin(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun logout() {
        // Only clear login status and current user details, not all registered users
        editor.remove(IS_LOGGED_IN)
        editor.remove(KEY_FIRST_NAME)
        editor.remove(KEY_LAST_NAME)
        editor.remove(KEY_EMAIL)
        editor.remove(KEY_PHONE)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

    // Function to check if a user with given email exists
    fun isUserRegistered(email: String): Boolean {
        val allEmails = sharedPreferences.getStringSet(KEY_REGISTERED_EMAILS, HashSet()) ?: HashSet()
        return allEmails.contains(email)
    }

    // Add a new email to the set of registered emails
    fun addRegisteredEmail(email: String) {
        val allEmails = sharedPreferences.getStringSet(KEY_REGISTERED_EMAILS, HashSet()) ?: HashSet()
        val updatedEmails = HashSet(allEmails)
        updatedEmails.add(email)
        editor.putStringSet(KEY_REGISTERED_EMAILS, updatedEmails)
        editor.apply()
    }

    // Function to validate login credentials
    fun validateUser(email: String, password: String): Boolean {
        // First check if the user exists
        if (!isUserRegistered(email)) {
            return false
        }

        // Get the user details from storage
        val user = getUserByEmail(email)

        // If we found the user and the password matches
        if (user != null && user.password == password) {
            // Set this user as the logged in user
            saveUserLoginSession(user)
            return true
        }

        // Fallback to the old method as a secondary check
        val storedEmail = sharedPreferences.getString(KEY_EMAIL, "")
        val storedPassword = sharedPreferences.getString(KEY_PASSWORD, "")
        return (email == storedEmail && password == storedPassword)
    }
}