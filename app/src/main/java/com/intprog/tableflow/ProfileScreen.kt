package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.app.AlertDialog
import android.view.View
import com.intprog.tableflow.model.ReservationManager
import com.intprog.tableflow.model.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class ProfileScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_screen)

        // Initialize SessionManager and ReservationManager
        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)

        // Check if user is logged in
        if (!sessionManager.checkLogin()) {
            // Redirect to login screen
            startActivity(Intent(this, LoginScreen::class.java))
            finish()
            return
        }

        val textViewFullName: TextView = findViewById(R.id.textViewFullName)
        val textViewPhone: TextView = findViewById(R.id.textViewPhone)
        val textViewEmail: TextView = findViewById(R.id.textViewEmail)
        val textViewReservationDateTime: TextView = findViewById(R.id.textViewReservationDateTime)
        val textViewReservationLocation: TextView = findViewById(R.id.textViewReservationLocation)
        val editProfileButton: Button = findViewById(R.id.editProfileButton)
        val logoutButton: Button = findViewById(R.id.logoutButton)
        val backButton: LinearLayout = findViewById(R.id.backButton)

        // navbar buttons
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notificationButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)
        val moreButton: LinearLayout = findViewById(R.id.moreButton)

        // Load user data
        loadUserData(sessionManager, textViewFullName, textViewPhone, textViewEmail)

        // Load upcoming reservation
        loadUpcomingReservation(sessionManager, reservationManager, textViewReservationDateTime, textViewReservationLocation)

        editProfileButton.setOnClickListener {
            val intent = Intent(this, EditProfileScreen::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            showLogoutConfirmationDialog(sessionManager)
        }

        backButton.setOnClickListener {
            // For now, just finish the activity
            finish()
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
        }

        notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationScreen::class.java)
            startActivity(intent)
        }

        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }

        moreButton.setOnClickListener {
            // Already on profile screen, do nothing or refresh
            // Since we're already on the ProfileScreen, we can just refresh the data
            loadUserData(sessionManager, textViewFullName, textViewPhone, textViewEmail)
            loadUpcomingReservation(sessionManager, reservationManager, textViewReservationDateTime, textViewReservationLocation)
        }
    }

    override fun onResume() {
        super.onResume()
        // Get references to views and managers again
        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)
        val textViewFullName: TextView = findViewById(R.id.textViewFullName)
        val textViewPhone: TextView = findViewById(R.id.textViewPhone)
        val textViewEmail: TextView = findViewById(R.id.textViewEmail)
        val textViewReservationDateTime: TextView = findViewById(R.id.textViewReservationDateTime)
        val textViewReservationLocation: TextView = findViewById(R.id.textViewReservationLocation)

        // Reload user data when returning to this screen
        loadUserData(sessionManager, textViewFullName, textViewPhone, textViewEmail)

        // Refresh reservation status
        reservationManager.refreshReservationStatuses(sessionManager.getUserDetails().email)

        // Reload upcoming reservation data
        loadUpcomingReservation(sessionManager, reservationManager, textViewReservationDateTime, textViewReservationLocation)
    }

    private fun loadUserData(sessionManager: SessionManager,
                             textViewFullName: TextView,
                             textViewPhone: TextView,
                             textViewEmail: TextView) {
        val user = sessionManager.getUserDetails()
        textViewFullName.text = "${user.firstName} ${user.lastName}"
        textViewPhone.text = user.phone
        textViewEmail.text = user.email
    }

    private fun loadUpcomingReservation(sessionManager: SessionManager,
                                        reservationManager: ReservationManager,
                                        textViewReservationDateTime: TextView,
                                        textViewReservationLocation: TextView) {
        val userEmail = sessionManager.getUserDetails().email

        // Get upcoming reservations for the user
        val upcomingReservations = reservationManager.getUpcomingReservations(userEmail)

        // Check if there are any upcoming reservations
        if (upcomingReservations.isNotEmpty()) {
            // Get the closest upcoming reservation (first in the list as they are already sorted)
            val closestReservation = upcomingReservations[0]

            // Format the date and time for display
            val inputDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val outputDateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())

            try {
                val date = inputDateFormat.parse(closestReservation.date)
                val formattedDate = date?.let { outputDateFormat.format(it).uppercase() } ?: closestReservation.date

                // Format the time (assuming time is already in 12-hour format with AM/PM)
                val displayDateTime = "$formattedDate | ${closestReservation.time}"

                // Update UI
                textViewReservationDateTime.text = displayDateTime
                textViewReservationLocation.text = closestReservation.restaurantName

                // Make reservation section visible
                textViewReservationDateTime.visibility = View.VISIBLE
                textViewReservationLocation.visibility = View.VISIBLE
            } catch (e: Exception) {
                // In case of date parsing error, just display the raw data
                textViewReservationDateTime.text = "${closestReservation.date} | ${closestReservation.time}"
                textViewReservationLocation.text = closestReservation.restaurantName
            }
        } else {
            // No upcoming reservations
            textViewReservationDateTime.text = "No upcoming reservations"
            textViewReservationLocation.text = ""
        }
    }

    private fun showLogoutConfirmationDialog(sessionManager: SessionManager) {
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