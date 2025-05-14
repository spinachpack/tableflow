package com.intprog.tableflow

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationManager
import com.intprog.tableflow.model.ReservationStatus
import com.intprog.tableflow.model.RestaurantDataProvider
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditReservation : Activity() {

    private lateinit var reservationManager: ReservationManager
    private lateinit var reservation: Reservation

    // UI elements
    private lateinit var restaurantNameText: TextView
    private lateinit var restaurantHours: TextView
    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private lateinit var guestsCount: TextView
    private lateinit var increaseGuests: ImageView
    private lateinit var decreaseGuests: ImageView

    private val calendar = Calendar.getInstance()
    private var selectedDate = ""
    private var selectedTime = ""
    private var guestCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reservation)

        // Initialize reservation manager
        reservationManager = ReservationManager(this)

        // Get reservation ID from intent
        val reservationId = intent.getStringExtra("reservation_id")
            ?: throw IllegalArgumentException("Reservation ID is required")

        // Get reservation details
        reservation = reservationManager.getReservationById(reservationId)
            ?: throw IllegalArgumentException("Reservation not found")

        // Initialize UI elements
        initializeViews()
        setupButtonListeners()

        // Load reservation data
        loadReservationData()
    }

    private fun initializeViews() {
        restaurantNameText = findViewById(R.id.restaurantName)
        restaurantHours = findViewById(R.id.restaurantHours)
        dateText = findViewById(R.id.dateText)
        timeText = findViewById(R.id.timeText)
        guestsCount = findViewById(R.id.guestsCount)
        increaseGuests = findViewById(R.id.increaseGuests)
        decreaseGuests = findViewById(R.id.decreaseGuests)

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun setupButtonListeners() {
        // Date layout click listener
        findViewById<android.view.View>(R.id.dateLayout).setOnClickListener {
            showDatePicker()
        }

        // Time layout click listener
        findViewById<android.view.View>(R.id.timeLayout).setOnClickListener {
            showTimePicker()
        }

        // Guest count buttons
        decreaseGuests.setOnClickListener {
            if (guestCount > 1) {
                guestCount--
                guestsCount.text = guestCount.toString()
            }
        }

        increaseGuests.setOnClickListener {
            if (guestCount < 10) {
                guestCount++
                guestsCount.text = guestCount.toString()
            }
        }

        // Update button
        findViewById<Button>(R.id.updateReservationButton).setOnClickListener {
            saveReservation()
        }

        // Navigation buttons
        findViewById<android.view.View>(R.id.homeButton).setOnClickListener {
            // Navigate to home
            finish()
            // added intent to return to dashboard screen -R
            startActivity(Intent(this, DashboardScreen::class.java))
        }

        findViewById<android.view.View>(R.id.notificationButton).setOnClickListener {
            // Navigate to notifications
            finish()
            // added intent -R
            startActivity(Intent(this, NotificationScreen::class.java))
        }

        findViewById<android.view.View>(R.id.bookingsButton).setOnClickListener {
            // Already on history screen
            finish()
            // added intent -R
            startActivity(Intent(this, HistoryScreen::class.java))
        }

        findViewById<android.view.View>(R.id.moreButton).setOnClickListener {
            // Navigate to more options
            finish()
            // added intent -R
            startActivity(Intent(this, SettingScreen::class.java))
        }
    }

    private fun loadReservationData() {
        // Get restaurant name
        val restaurant = RestaurantDataProvider.getRestaurantById(reservation.restaurantId)
        restaurantNameText.text = restaurant?.name ?: reservation.restaurantName

        // Set restaurant hours (this would come from the actual restaurant data)
        restaurantHours.text = "10:00 AM - 9:00 PM"

        // Set date and time
        selectedDate = reservation.date
        selectedTime = reservation.time
        dateText.text = selectedDate
        timeText.text = selectedTime

        // Set guests
        guestCount = reservation.guests
        guestsCount.text = guestCount.toString()
    }

    private fun showDatePicker() {
        // Parse current date from reservation
        try {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = dateFormat.parse(reservation.date)
            if (date != null) {
                calendar.time = date
            }
        } catch (e: Exception) {
            // Use current date if parsing fails
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                calendar.set(Calendar.YEAR, selectedYear)
                calendar.set(Calendar.MONTH, selectedMonth)
                calendar.set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)

                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)
                dateText.text = selectedDate
            },
            year,
            month,
            day
        )

        // Set minimum date to today
        val today = Calendar.getInstance()
        datePickerDialog.datePicker.minDate = today.timeInMillis

        datePickerDialog.show()
    }

    private fun showTimePicker() {
        // Parse current time from reservation
        try {
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = timeFormat.parse(reservation.time)
            if (time != null) {
                calendar.time = time
            }
        } catch (e: Exception) {
            // Use current time if parsing fails
        }

        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                selectedTime = timeFormat.format(calendar.time)
                timeText.text = selectedTime
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun saveReservation() {
        if (selectedDate.isEmpty() || selectedTime.isEmpty()) {
            Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show()
            return
        }

        // Create updated reservation
        val updatedReservation = reservation.copy(
            date = selectedDate,
            time = selectedTime,
            guests = guestCount
        )

        // Save changes
        reservationManager.updateReservation(updatedReservation)

        Toast.makeText(this, "Reservation updated successfully", Toast.LENGTH_SHORT).show()

        // Return to history screen
        finish()
    }
}