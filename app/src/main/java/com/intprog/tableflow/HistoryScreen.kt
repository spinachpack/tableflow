package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.adapter.ReservationHistoryAdapter
import com.intprog.tableflow.model.ReservationManager
import com.intprog.tableflow.model.ReservationStatus
import com.intprog.tableflow.model.SessionManager

class HistoryScreen : Activity() {

    private lateinit var historyAdapter: ReservationHistoryAdapter
    private lateinit var reservationManager: ReservationManager
    private lateinit var sessionManager: SessionManager
    private lateinit var emptyHistoryView: TextView
    private lateinit var historyListView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_screen)

        sessionManager = SessionManager(this)
        reservationManager = ReservationManager(this)

        // Initialize views
        historyListView = findViewById(R.id.historyListView)
        emptyHistoryView = findViewById(R.id.emptyHistoryView)

        // Set up navigation
        setupNavigation()

        // Initialize adapter with empty list
        historyAdapter = ReservationHistoryAdapter(
            this,
            emptyList(),
            onCancelClickListener = { reservationId ->
                reservationManager.updateReservationStatus(reservationId, ReservationStatus.CANCELLED)
                loadHistory()
            },
            onEditClickListener = { reservationId ->
                // Launch edit reservation activity
                Toast.makeText(this, "Edit feature coming soon", Toast.LENGTH_SHORT).show()
            },
            onPreOrderClickListener = { reservationId ->
                // Launch pre-order activity
                Toast.makeText(this, "Pre-order feature coming soon", Toast.LENGTH_SHORT).show()
            }
        )

        historyListView.adapter = historyAdapter

        // Load history data
        loadHistory()
    }

    private fun setupNavigation() {
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
            // Already on the history screen
        }

        // Back button (if needed)
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            val intent = Intent(this, DashboardScreen::class.java)
            startActivity(intent)
        }
    }

    private fun loadHistory() {
        // Get current user
        val currentUser = sessionManager.getUserDetails()

        // Update reservation statuses
        reservationManager.refreshReservationStatuses(currentUser.email)

        // Get all reservations for the current user
        val allReservations = reservationManager.getUserReservations(currentUser.email)

        if (allReservations.isEmpty()) {
            historyListView.visibility = View.GONE
            emptyHistoryView.visibility = View.VISIBLE
            return
        }

        historyListView.visibility = View.VISIBLE
        emptyHistoryView.visibility = View.GONE

        // Sort reservations by creation date (newest first)
        val sortedReservations = allReservations.sortedByDescending { it.createdAt }

        // Update adapter with new data
        historyAdapter.updateReservations(sortedReservations)
    }

    override fun onResume() {
        super.onResume()
        // Refresh history when screen is resumed
        loadHistory()
    }
}