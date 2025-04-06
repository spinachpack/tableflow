package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.Restaurant
import com.intprog.tableflow.model.RestaurantDataProvider
import java.text.SimpleDateFormat
import java.util.*

class RestaurantDetailScreen : Activity() {

    private lateinit var selectedRestaurant: Restaurant
    private var selectedTimeSlot: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_detail_screen)

        // Get the restaurant ID passed from the previous screen
        val restaurantId = intent.getStringExtra("RESTAURANT_ID") ?: return

        // Fetch restaurant details using the RestaurantDataProvider
        selectedRestaurant = RestaurantDataProvider.getRestaurantById(restaurantId)
            ?: return // Handle case if the restaurant is not found

        setupUI()
        setupClickListeners()
        createTimeSlots()
    }

    private fun setupUI() {
        findViewById<TextView>(R.id.restaurantName).text = selectedRestaurant.name
        findViewById<ImageView>(R.id.restaurantImage).setImageResource(selectedRestaurant.imageResource)

        findViewById<TextView>(R.id.restaurantAddress).text =
            "${selectedRestaurant.address}, ${selectedRestaurant.city}\n${selectedRestaurant.zipCode} ${selectedRestaurant.country}"

        findViewById<TextView>(R.id.restaurantHours).text =
            "${selectedRestaurant.openingHours} - ${selectedRestaurant.closingHours} Daily"

        findViewById<TextView>(R.id.restaurantDescription).text = selectedRestaurant.description

        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.selectedDate).text = dateFormat.format(Date())

        findViewById<TextView>(R.id.selectedTime).text = "12:15"
        findViewById<TextView>(R.id.selectedGuests).text = "2"
    }

    private fun setupClickListeners() {
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        findViewById<TextView>(R.id.getDirections).setOnClickListener {
            // Open maps with the restaurant location
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.menuButton).setOnClickListener {
            // Show restaurant menu
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<TextView>(R.id.readMore).setOnClickListener {
            // Expand description
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.dateSelector).setOnClickListener {
            // Show date picker
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.timeSelector).setOnClickListener {
            // Show time picker
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<View>(R.id.guestsSelector).setOnClickListener {
            // Show guests picker
            Toast.makeText(this,"Feature unavailable at the moment", Toast.LENGTH_LONG).show()
        }

        findViewById<Button>(R.id.makeReservationButton).setOnClickListener {
            if (selectedTimeSlot != null) {
                val intent = Intent(this, ReservationFormScreen::class.java).apply {
                    putExtra("RESTAURANT_ID", selectedRestaurant.id)
                    putExtra("RESERVATION_DATE", findViewById<TextView>(R.id.selectedDate).text.toString())
                    putExtra("RESERVATION_TIME", selectedTimeSlot)
                    putExtra("RESERVATION_GUESTS", findViewById<TextView>(R.id.selectedGuests).text.toString())
                }
                startActivity(intent)
            } else {
                // Show message to select a time slot
            }
        }
    }

    private fun createTimeSlots() {
        val timeSlotsGrid = findViewById<GridLayout>(R.id.timeSlotsGrid)
        timeSlotsGrid.removeAllViews()

        val timeSlots = arrayOf("11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00")

        for (i in timeSlots.indices) {
            val button = Button(this).apply {
                text = timeSlots[i]
                setBackgroundColor(Color.parseColor("#333333"))
                setTextColor(Color.WHITE)

                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(i % 4, 1f)
                    rowSpec = GridLayout.spec(i / 4)
                    setMargins(8, 8, 8, 8)
                }
                layoutParams = params

                setOnClickListener {
                    for (j in 0 until timeSlotsGrid.childCount) {
                        val otherButton = timeSlotsGrid.getChildAt(j) as Button
                        otherButton.setBackgroundColor(Color.parseColor("#333333"))
                        otherButton.setTextColor(Color.WHITE)
                    }

                    setBackgroundColor(Color.parseColor("#FFA500"))
                    setTextColor(Color.WHITE)

                    selectedTimeSlot = text.toString()
                }

                if (text == "12:15") {
                    setBackgroundColor(Color.parseColor("#FFA500"))
                    selectedTimeSlot = "12:15"
                }
            }

            timeSlotsGrid.addView(button)
        }
    }
}
