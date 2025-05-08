package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import com.intprog.tableflow.adapter.NotificationAdapter
import com.intprog.tableflow.model.ReservationManager
import com.intprog.tableflow.model.SessionManager

class NotificationScreen : Activity() {

    private lateinit var notificationAdapter: NotificationAdapter
    private lateinit var reservationManager: ReservationManager
    private lateinit var sessionManager: SessionManager
    private lateinit var emptyNotificationsView: TextView
    private lateinit var notificationsListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)

        sessionManager = SessionManager(this)
        reservationManager = ReservationManager(this)

        // Initialize views
        notificationsListView = findViewById(R.id.notificationsListView)
        emptyNotificationsView = findViewById(R.id.emptyNotificationsView)

        // Set up navigation
        setupNavigation()

        // Initialize adapter with empty list
        notificationAdapter = NotificationAdapter(this, emptyList())
        notificationsListView.adapter = notificationAdapter

        // Load notifications data
        loadNotifications()
    }

    private fun setupNavigation() {
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
            // Already on notifications screen
        }
        moreButton.setOnClickListener {
            val intent = Intent(this, SettingScreen::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }
    }

    private fun loadNotifications() {
        // Get current user
        val currentUser = sessionManager.getUserDetails()

        // Update reservation statuses
        reservationManager.refreshReservationStatuses(currentUser.email)

        // Get all reservations for the current user
        val allReservations = reservationManager.getUserReservations(currentUser.email)

        if (allReservations.isEmpty()) {
            notificationsListView.visibility = View.GONE
            emptyNotificationsView.visibility = View.VISIBLE
            return
        }

        notificationsListView.visibility = View.VISIBLE
        emptyNotificationsView.visibility = View.GONE

        // Sort reservations by creation date (newest first)
        val sortedReservations = allReservations.sortedByDescending { it.createdAt }

        // Update adapter with new data
        notificationAdapter.updateReservations(sortedReservations)
    }

    override fun onResume() {
        super.onResume()
        // Refresh notifications when screen is resumed
        loadNotifications()
    }
}