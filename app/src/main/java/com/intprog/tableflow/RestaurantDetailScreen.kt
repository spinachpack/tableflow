package com.intprog.tableflow

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.Restaurant
import com.intprog.tableflow.model.RestaurantDataProvider
import java.text.SimpleDateFormat
import java.util.*

class RestaurantDetailScreen : Activity() {

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail_screen)

        // Get the restaurant ID passed from the previous screen
        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return

        // Fetch restaurant details using the RestaurantDataProvider
        val selectedRestaurant = RestaurantDataProvider.getRestaurantById(restaurantId)
            ?: return // Handle case if the restaurant is not found

        setupUI(selectedRestaurant)
        setupClickListeners(selectedRestaurant)
    }

    private fun setupUI(selectedRestaurant: Restaurant) {
        findViewById<TextView>(R.id.restaurantName).text = selectedRestaurant.name
        findViewById<ImageView>(R.id.restaurantImage).setImageResource(selectedRestaurant.imageResource)

        findViewById<TextView>(R.id.restaurantAddress).text =
            "${selectedRestaurant.address}, ${selectedRestaurant.city}\n${selectedRestaurant.zipCode} ${selectedRestaurant.country}"

        findViewById<TextView>(R.id.restaurantHours).text =
            "${selectedRestaurant.openingHours} - ${selectedRestaurant.closingHours} Daily"

        findViewById<TextView>(R.id.restaurantDescription).text = selectedRestaurant.description

        val selectedDateTextView: TextView = findViewById(R.id.selectedDate)
        val selectedTimeTextView: TextView = findViewById(R.id.selectedTime)
        val selectedGuestsTextView: TextView = findViewById(R.id.selectedGuests)

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        selectedDateTextView.text = dateFormat.format(Date())

        // Set default time
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val defaultTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12)
            set(Calendar.MINUTE, 15)
        }
        selectedTimeTextView.text = timeFormat.format(defaultTime.time)

        // Default number of guests
        selectedGuestsTextView.text = "2"
    }

    private fun setupClickListeners(selectedRestaurant: Restaurant) {
        val selectedDateTextView: TextView = findViewById(R.id.selectedDate)
        val selectedTimeTextView: TextView = findViewById(R.id.selectedTime)
        val selectedGuestsTextView: TextView = findViewById(R.id.selectedGuests)

        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.getDirections).setOnClickListener {
            // Open maps with the restaurant location
            Toast.makeText(this, "Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.menuButton).setOnClickListener {
            // Show restaurant menu
            Toast.makeText(this, "Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<TextView>(R.id.readMore).setOnClickListener {
            // Expand description
            Toast.makeText(this, "Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.dateSelector).setOnClickListener {
            showDatePicker(selectedDateTextView)
        }

        findViewById<View>(R.id.timeSelector).setOnClickListener {
            showTimePicker(selectedTimeTextView)
        }

        findViewById<View>(R.id.guestsSelector).setOnClickListener {
            showSimpleGuestSelector(selectedGuestsTextView)
        }

        findViewById<Button>(R.id.makeReservationButton).setOnClickListener {
            if (isTimeSlotValid(selectedDateTextView, selectedTimeTextView)) {
                val intent = Intent(this, ReservationFormScreen::class.java).apply {
                    putExtra("RESTAURANT_ID", selectedRestaurant.id)
                    putExtra("RESERVATION_DATE", selectedDateTextView.text.toString())
                    putExtra("RESERVATION_TIME", selectedTimeTextView.text.toString())
                    putExtra("RESERVATION_GUESTS", selectedGuestsTextView.text.toString())
                }
                startActivity(intent)
            } else {
                Toast.makeText(this, "Reservations must be made at least 3 hours in advance", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun isTimeSlotValid(selectedDateTextView: TextView, selectedTimeTextView: TextView): Boolean {
        // Get the current date and time
        val currentCalendar = Calendar.getInstance()

        // Get the selected date
        val selectedDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        selectedDate.time = dateFormat.parse(selectedDateTextView.text.toString()) ?: Date()

        // If selected date is in the future, it's valid
        if (selectedDate.get(Calendar.YEAR) > currentCalendar.get(Calendar.YEAR) ||
            (selectedDate.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
                    selectedDate.get(Calendar.DAY_OF_YEAR) > currentCalendar.get(Calendar.DAY_OF_YEAR))) {
            return true
        }

        // If it's today, check if the time is at least 3 hours from now
        if (selectedDate.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR) &&
            selectedDate.get(Calendar.DAY_OF_YEAR) == currentCalendar.get(Calendar.DAY_OF_YEAR)) {

            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val selectedTimeCalendar = Calendar.getInstance()
            selectedTimeCalendar.time = timeFormat.parse(selectedTimeTextView.text.toString()) ?: Date()

            selectedDate.set(Calendar.HOUR_OF_DAY, selectedTimeCalendar.get(Calendar.HOUR_OF_DAY))
            selectedDate.set(Calendar.MINUTE, selectedTimeCalendar.get(Calendar.MINUTE))

            // Add 3 hours to current time
            val threeHoursLater = Calendar.getInstance()
            threeHoursLater.add(Calendar.HOUR_OF_DAY, 3)

            return selectedDate.after(threeHoursLater)
        }

        return false
    }

    private fun showDatePicker(selectedDateTextView: TextView) {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                selectedDateTextView.text = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        // Set minimum date to today
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000

        // Set maximum date to 30 days from now
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.DAY_OF_MONTH, 30)
        datePickerDialog.datePicker.maxDate = maxDate.timeInMillis

        datePickerDialog.show()
    }

    private fun showTimePicker(selectedTimeTextView: TextView) {
        // Get current hour and minute
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)

        // Create TimePickerDialog
        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val timeCalendar = Calendar.getInstance()
                timeCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                timeCalendar.set(Calendar.MINUTE, selectedMinute)

                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                selectedTimeTextView.text = timeFormat.format(timeCalendar.time)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    private fun showSimpleGuestSelector(selectedGuestsTextView: TextView) {
        val guestOptions = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")

        val builder = android.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_MinWidth)
        builder.setTitle("Select number of guests")

        builder.setItems(guestOptions) { dialog, which ->
            selectedGuestsTextView.text = guestOptions[which]
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.window?.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.show()
    }
}