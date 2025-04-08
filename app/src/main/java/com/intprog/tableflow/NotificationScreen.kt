package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.intprog.tableflow.model.ReservationStatus
import java.text.SimpleDateFormat
import java.util.*

class NotificationScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)

        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)

        // Initialize views
        val notificationsContainer: LinearLayout = findViewById(R.id.notificationsContainer)

        // Back button (if needed)
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            finish()
        }

        // Navigation buttons
        val homeButton: LinearLayout = findViewById(R.id.homeButton)
        val notificationButton: LinearLayout = findViewById(R.id.notificationButton)
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

        // Load notifications
        loadNotifications(sessionManager, reservationManager, notificationsContainer)
    }

    private fun loadNotifications(sessionManager: SessionManager,
                                  reservationManager: ReservationManager,
                                  notificationsContainer: LinearLayout) {
        // Clear existing notifications
        notificationsContainer.removeAllViews()

        // Get current user
        val currentUser = sessionManager.getUserDetails()

        // Update reservation statuses
        reservationManager.refreshReservationStatuses(currentUser.email)

        // Get all reservations for the current user
        val allReservations = reservationManager.getUserReservations(currentUser.email)

        if (allReservations.isEmpty()) {
            // Show "No notifications" message
            val emptyView = TextView(this).apply {
                text = "You have no notifications"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.white))
                setPadding(32, 64, 32, 32)
            }
            notificationsContainer.addView(emptyView)
            return
        }

        // Group reservations by restaurant
        val reservationsByRestaurant = allReservations.groupBy { it.restaurantName }

        // Create notification items
        val inflater = LayoutInflater.from(this)
        val now = Calendar.getInstance().timeInMillis

        reservationsByRestaurant.forEach { (restaurantName, reservations) ->
            reservations.sortedByDescending { it.createdAt }.forEach { reservation ->
                val notificationView = inflater.inflate(R.layout.item_notification, notificationsContainer, false)

                // Set restaurant name
                notificationView.findViewById<TextView>(R.id.restaurantName).text = restaurantName

                // Set notification time
                val timeDiff = getTimeDifferenceText(reservation.createdAt, now)
                notificationView.findViewById<TextView>(R.id.notificationTime).text = timeDiff

                // Set notification message based on status
                val messageView = notificationView.findViewById<TextView>(R.id.notificationMessage)
                when (reservation.status) {
                    ReservationStatus.RESERVED -> messageView.text = "Your Table is Booked"
                    ReservationStatus.COMPLETED -> messageView.text = "Thankyou For Visiting. Please Come Again"
                    ReservationStatus.CANCELLED -> messageView.text = "Your booking has been canceled"
                }

                // Set reservation details for booked/reserved reservations
                if (reservation.status == ReservationStatus.RESERVED) {
                    // Date and time
                    val dateTimeContainer = notificationView.findViewById<LinearLayout>(R.id.dateTimeContainer)
                    dateTimeContainer.visibility = View.VISIBLE

                    val dateTime = formatDateTime(reservation.date, reservation.time)
                    notificationView.findViewById<TextView>(R.id.reservationDateTime).text = dateTime

                    // Guests
                    val guestsContainer = notificationView.findViewById<LinearLayout>(R.id.guestsContainer)
                    guestsContainer.visibility = View.VISIBLE
                    notificationView.findViewById<TextView>(R.id.reservationGuests).text = "${reservation.guests} Guests"
                } else {
                    // Hide details for completed/cancelled reservations
                    notificationView.findViewById<LinearLayout>(R.id.dateTimeContainer).visibility = View.GONE
                    notificationView.findViewById<LinearLayout>(R.id.guestsContainer).visibility = View.GONE
                }

                // Add to container
                notificationsContainer.addView(notificationView)
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
        // Refresh notifications when screen is resumed
        val sessionManager = SessionManager(this)
        val reservationManager = ReservationManager(this)
        val notificationsContainer: LinearLayout = findViewById(R.id.notificationsContainer)
        loadNotifications(sessionManager, reservationManager, notificationsContainer)
    }
}