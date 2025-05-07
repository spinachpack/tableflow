package com.intprog.tableflow.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.intprog.tableflow.R
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationStatus
import java.text.SimpleDateFormat
import java.util.*

class ReservationHistoryAdapter(
    private val context: Context,
    private var reservations: List<Reservation>,
    private val onCancelClickListener: (String) -> Unit,
    private val onEditClickListener: (String) -> Unit,
    private val onPreOrderClickListener: (String) -> Unit
) : BaseAdapter() {

    override fun getCount(): Int = reservations.size

    override fun getItem(position: Int): Any = reservations[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val reservation = reservations[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)

        // Set restaurant name
        view.findViewById<TextView>(R.id.restaurantName).text = reservation.restaurantName

        // Set time difference
        val now = Calendar.getInstance().timeInMillis
        val timeDiff = getTimeDifferenceText(reservation.createdAt, now)
        view.findViewById<TextView>(R.id.historyTime).text = timeDiff

        // Set status
        val statusView = view.findViewById<TextView>(R.id.reservationStatus)
        when (reservation.status) {
            ReservationStatus.RESERVED -> {
                statusView.text = "Reserved"
                statusView.setTextColor(context.resources.getColor(android.R.color.holo_orange_light))
            }
            ReservationStatus.COMPLETED -> {
                statusView.text = "Completed"
                statusView.setTextColor(context.resources.getColor(android.R.color.holo_green_light))
            }
            ReservationStatus.CANCELLED -> {
                statusView.text = "Cancelled"
                statusView.setTextColor(context.resources.getColor(android.R.color.holo_red_light))
            }
        }

        // Set reservation details
        val dateTime = formatDateTime(reservation.date, reservation.time)
        view.findViewById<TextView>(R.id.reservationDateTime).text = dateTime
        view.findViewById<TextView>(R.id.reservationGuests).text = "${reservation.guests} Guests"

        // Action buttons
        val cancelButton = view.findViewById<TextView>(R.id.cancelButton)
        val editButton = view.findViewById<TextView>(R.id.editButton)
        val preOrderButton = view.findViewById<TextView>(R.id.preOrderButton)

        if (reservation.status == ReservationStatus.RESERVED) {
            // Show action buttons for reserved reservations
            cancelButton.visibility = View.VISIBLE
            editButton.visibility = View.VISIBLE
            preOrderButton.visibility = View.VISIBLE

            // Cancel reservation
            cancelButton.setOnClickListener {
                onCancelClickListener(reservation.id)
            }

            // Edit reservation
            editButton.setOnClickListener {
                onEditClickListener(reservation.id)
            }

            // Pre-order food
            preOrderButton.setOnClickListener {
                onPreOrderClickListener(reservation.id)
            }
        } else {
            // Hide action buttons for completed/cancelled reservations
            cancelButton.visibility = View.GONE
            editButton.visibility = View.GONE
            preOrderButton.visibility = View.GONE
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