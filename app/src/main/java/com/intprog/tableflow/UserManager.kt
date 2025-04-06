package com.intprog.tableflow

import android.content.Context
import android.content.SharedPreferences

/**
 * Helper class to manage user data with SharedPreferences
 */
class UserManager private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "UserPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_EMAIL = "email"
        private const val KEY_FIRST_NAME = "firstName"
        private const val KEY_LAST_NAME = "lastName"
        private const val KEY_PHONE = "phone"
        private const val KEY_PASSWORD = "password"
        private const val KEY_PROFILE_IMAGE = "profileImage"

        private var instance: UserManager? = null

        // Singleton pattern to get UserManager instance
        @Synchronized
        fun getInstance(context: Context): UserManager {
            if (instance == null) {
                instance = UserManager(context.applicationContext)
            }
            return instance!!
        }
    }

    /**
     * Save user data during registration or profile update
     */
    fun saveUserData(email: String, firstName: String, lastName: String, phone: String, password: String) {
        with(sharedPreferences.edit()) {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putString(KEY_EMAIL, email)
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
            putString(KEY_PHONE, phone)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    /**
     * Save profile image path - can be local file path or URI string
     */
    fun saveProfileImagePath(imagePath: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_PROFILE_IMAGE, imagePath)
            apply()
        }
    }

    /**
     * Login user with email and password
     * @return true if credentials match stored data, false otherwise
     */
    fun loginUser(email: String, password: String): Boolean {
        val storedEmail = sharedPreferences.getString(KEY_EMAIL, "")
        val storedPassword = sharedPreferences.getString(KEY_PASSWORD, "")

        if (storedEmail == email && storedPassword == password) {
            sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, true).apply()
            return true
        }
        return false
    }

    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /**
     * Log out user
     */
    fun logoutUser() {
        sharedPreferences.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }

    /**
     * Clear all user data (for complete logout or account deletion)
     */
    fun clearUserData() {
        sharedPreferences.edit().clear().apply()
    }

    // Getters for user information
    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }

    fun getFirstName(): String {
        return sharedPreferences.getString(KEY_FIRST_NAME, "") ?: ""
    }

    fun getLastName(): String {
        return sharedPreferences.getString(KEY_LAST_NAME, "") ?: ""
    }

    fun getFullName(): String {
        return "${getFirstName()} ${getLastName()}"
    }

    fun getPhone(): String {
        return sharedPreferences.getString(KEY_PHONE, "") ?: ""
    }

    fun getProfileImagePath(): String {
        return sharedPreferences.getString(KEY_PROFILE_IMAGE, "") ?: ""
    }

    /**
     * Update user profile information
     */
    fun updateProfile(firstName: String, lastName: String, phone: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_FIRST_NAME, firstName)
            putString(KEY_LAST_NAME, lastName)
            putString(KEY_PHONE, phone)
            apply()
        }
    }

    /**
     * Update password
     */
    fun updatePassword(newPassword: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_PASSWORD, newPassword)
            apply()
        }
    }
}
