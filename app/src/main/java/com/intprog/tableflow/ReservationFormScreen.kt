package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class ReservationFormScreen : Activity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var reservationManager: ReservationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reservation_form_screen)

        sessionManager = SessionManager(this)
        reservationManager = ReservationManager(this)

        // Check if user is logged in
        if (!sessionManager.checkLogin()) {
            // Redirect to login
            Toast.makeText(this, "Please login to make a reservation", Toast.LENGTH_LONG).show()
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Get data from intent with safe defaults
        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: ""
        val date = intent.getStringExtra("RESERVATION_DATE") ?: getCurrentDate()
        val time = intent.getStringExtra("RESERVATION_TIME") ?: "12:00"
        val guests = intent.getStringExtra("RESERVATION_GUESTS") ?: "1"

        // Format the date for display
        val displayDate = formatDate(date)
        val displayTime = formatTime(time)

        // Set UI elements
        findViewById<TextView>(R.id.reservationDateTime).text = "$displayDate | $displayTime"
        findViewById<TextView>(R.id.reservationGuests).text = "$guests Guests"

        // Pre-fill user details
        val currentUser = sessionManager.getUserDetails()
        findViewById<EditText>(R.id.fullNameInput).setText("${currentUser.firstName} ${currentUser.lastName}")
        findViewById<EditText>(R.id.phoneNumberInput).setText(currentUser.phone)
        findViewById<EditText>(R.id.emailInput).setText(currentUser.email)

        // Back button
        findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Reserve Now button
        findViewById<Button>(R.id.reserveNowButton).setOnClickListener {
            try {
                // Validate form
                val fullName = findViewById<EditText>(R.id.fullNameInput).text.toString()
                val phone = findViewById<EditText>(R.id.phoneNumberInput).text.toString()

                if (fullName.isBlank() || phone.isBlank()) {
                    Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Check if restaurant ID is empty
                if (restaurantId.isBlank()) {
                    Toast.makeText(this, "Invalid restaurant selected", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Create reservation
                val reservation = reservationManager.createReservation(
                    restaurantId = restaurantId,
                    userEmail = currentUser.email,
                    date = date,
                    time = time,
                    guests = guests.toIntOrNull() ?: 1
                )

                // Show success message
                Toast.makeText(this, "Reservation successfully created!", Toast.LENGTH_LONG).show()

                // Go to confirmation screen or home screen
                val intent = Intent(this, DashboardScreen::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                Toast.makeText(this, "Error creating reservation: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun formatDate(dateStr: String): String {
        try {
            val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateStr) ?: return dateStr
            return outputFormat.format(date)
        } catch (e: Exception) {
            return dateStr
        }
    }

    private fun formatTime(timeStr: String): String {
        try {
            val inputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val time = inputFormat.parse(timeStr) ?: return timeStr
            return outputFormat.format(time)
        } catch (e: Exception) {
            return timeStr
        }
    }
}