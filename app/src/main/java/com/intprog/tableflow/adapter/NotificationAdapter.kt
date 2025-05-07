package com.intprog.tableflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.intprog.tableflow.R
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationStatus
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val context: Context,
    private var reservations: List<Reservation>
) : BaseAdapter() {

    override fun getCount(): Int = reservations.size

    override fun getItem(position: Int): Any = reservations[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val reservation = reservations[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)

        // Set restaurant name
        view.findViewById<TextView>(R.id.restaurantName).text = reservation.restaurantName

        // Set notification time
        val now = Calendar.getInstance().timeInMillis
        val timeDiff = getTimeDifferenceText(reservation.createdAt, now)
        view.findViewById<TextView>(R.id.notificationTime).text = timeDiff

        // Set notification message based on status
        val messageView = view.findViewById<TextView>(R.id.notificationMessage)
        when (reservation.status) {
            ReservationStatus.RESERVED -> messageView.text = "Your Table is Booked"
            ReservationStatus.COMPLETED -> messageView.text = "Thankyou For Visiting. Please Come Again"
            ReservationStatus.CANCELLED -> messageView.text = "Your booking has been canceled"
        }

        // Set reservation details for booked/reserved reservations
        if (reservation.status == ReservationStatus.RESERVED) {
            // Date and time
            val dateTimeContainer = view.findViewById<LinearLayout>(R.id.dateTimeContainer)
            dateTimeContainer.visibility = View.VISIBLE

            val dateTime = formatDateTime(reservation.date, reservation.time)
            view.findViewById<TextView>(R.id.reservationDateTime).text = dateTime

            // Guests
            val guestsContainer = view.findViewById<LinearLayout>(R.id.guestsContainer)
            guestsContainer.visibility = View.VISIBLE
            view.findViewById<TextView>(R.id.reservationGuests).text = "${reservation.guests} Guests"
        } else {
            // Hide details for completed/cancelled reservations
            view.findViewById<LinearLayout>(R.id.dateTimeContainer).visibility = View.GONE
            view.findViewById<LinearLayout>(R.id.guestsContainer).visibility = View.GONE
        }

        return view
    }

    // Update reservations data
    fun updateReservations(newReservations: List<Reservation>) {
        reservations = newReservations
        notifyDataSetChanged()
    }

    // Helper method for time difference
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

    // Helper method to format date and time
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
}