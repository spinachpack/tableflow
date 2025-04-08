package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.ReservationStatus
import java.text.SimpleDateFormat
import java.util.*

class HistoryScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_screen)

        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)

        // Initialize views
        val historyContainer: LinearLayout = findViewById(R.id.historyContainer)

        // Navbar
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notifactionButton)
        val moreButton: LinearLayout = findViewById(R.id.moreButton)
        val historyButton: LinearLayout = findViewById(R.id.historyButton)

        homeButton.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
        }
        notificationButton.setOnClickListener {
            val intent = Intent(this, NotificationScreen::class.java)
            startActivity(intent)
        }
        moreButton.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }

        // Back button (if needed)
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
        }

        // Load history
        loadHistory(sessionManager, reservationManager, historyContainer)
    }

    private fun loadHistory(sessionManager: SessionManager,
                            reservationManager: ReservationManager,
                            historyContainer: LinearLayout) {
        // Clear existing history items
        historyContainer.removeAllViews()

        // Get current user
        val currentUser = sessionManager.getUserDetails()

        // Update reservation statuses
        reservationManager.refreshReservationStatuses(currentUser.email)

        // Get all reservations for the current user
        val allReservations = reservationManager.getUserReservations(currentUser.email)

        if (allReservations.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "You have no reservation history"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(32, 64, 32, 32)
            }
            historyContainer.addView(emptyView)
            return
        }

        // Group reservations by restaurant
        val reservationsByRestaurant = allReservations.groupBy { it.restaurantName }

        // Create history items
        val inflater = LayoutInflater.from(this)
        val now = Calendar.getInstance().timeInMillis

        reservationsByRestaurant.forEach { (restaurantName, reservations) ->
            reservations.sortedByDescending { it.createdAt }.forEach { reservation ->
                val historyView = inflater.inflate(R.layout.item_history, historyContainer, false)

                historyView.findViewById<TextView>(R.id.restaurantName).text = restaurantName

                val timeDiff = getTimeDifferenceText(reservation.createdAt, now)
                historyView.findViewById<TextView>(R.id.historyTime).text = timeDiff

                val statusView = historyView.findViewById<TextView>(R.id.reservationStatus)
                when (reservation.status) {
                    ReservationStatus.RESERVED -> {
                        statusView.text = "Reserved"
                        statusView.setTextColor(resources.getColor(android.R.color.holo_orange_light))
                    }
                    ReservationStatus.COMPLETED -> {
                        statusView.text = "Completed"
                        statusView.setTextColor(resources.getColor(android.R.color.holo_green_light))
                    }
                    ReservationStatus.CANCELLED -> {
                        statusView.text = "Cancelled"
                        statusView.setTextColor(resources.getColor(android.R.color.holo_red_light))
                    }
                }

                // Set reservation details
                val dateTime = formatDateTime(reservation.date, reservation.time)
                historyView.findViewById<TextView>(R.id.reservationDateTime).text = dateTime
                historyView.findViewById<TextView>(R.id.reservationGuests).text = "${reservation.guests} Guests"

                // Action buttons
                val cancelButton = historyView.findViewById<TextView>(R.id.cancelButton)
                val editButton = historyView.findViewById<TextView>(R.id.editButton)
                val preOrderButton = historyView.findViewById<TextView>(R.id.preOrderButton)

                if (reservation.status == ReservationStatus.RESERVED) {
                    // Show action buttons for reserved reservations
                    cancelButton.visibility = View.VISIBLE
                    editButton.visibility = View.VISIBLE
                    preOrderButton.visibility = View.VISIBLE

                    // Cancel reservation
                    cancelButton.setOnClickListener {
                        reservationManager.updateReservationStatus(reservation.id, ReservationStatus.CANCELLED)
                        loadHistory(sessionManager, reservationManager, historyContainer) // Refresh the list
                    }

                    // Edit reservation
                    editButton.setOnClickListener {
                        // Launch edit reservation activity
                        Toast.makeText(this, "Edit feature coming soon", Toast.LENGTH_SHORT).show()
                    }

                    // Pre-order food
                    preOrderButton.setOnClickListener {
                        // Launch pre-order activity
                        Toast.makeText(this, "Pre-order feature coming soon", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Hide action buttons for completed/cancelled reservations
                    cancelButton.visibility = View.GONE
                    editButton.visibility = View.GONE
                    preOrderButton.visibility = View.GONE
                }

                // Add to container
                historyContainer.addView(historyView)
            }
        }
    }

    private fun getTimeDifferenceText(timestamp: Long, now: Long): String {
        val diff = now - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30

        return when {
            months > 0 -> "$months Month${if (months > 1) "s" else ""} ago"
            days > 0 -> "$days Day${if (days > 1) "s" else ""} ago"
            hours > 0 -> "$hours hr${if (hours > 1) "s" else ""} ago"
            minutes > 0 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
            else -> "Just now"
        }
    }

    private fun formatDateTime(date: String, time: String): String {
        try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val dateObj = dateFormat.parse(date) ?: return "$date | $time"

            val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
            val formattedDate = outputFormat.format(dateObj)

            // Format time if needed
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val timeObj = timeFormat.parse(time) ?: return "$formattedDate | $time"

            val outputTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val formattedTime = outputTimeFormat.format(timeObj)

            return "$formattedDate | $formattedTime"
        } catch (e: Exception) {
            return "$date | $time"
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh history when screen is resumed
        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)
        val historyContainer: LinearLayout = findViewById(R.id.historyContainer)
        loadHistory(sessionManager, reservationManager, historyContainer)
    }
}