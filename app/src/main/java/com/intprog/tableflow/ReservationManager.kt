package com.intprog.tableflow

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationStatus
import com.intprog.tableflow.model.RestaurantDataProvider
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class ReservationManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ReservationPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    companion object {
        private const val KEY_RESERVATIONS = "reservations"
    }

    // Create a new reservation
    fun createReservation(
        restaurantId: String,
        userEmail: String,
        date: String,
        time: String,
        guests: Int
    ): Reservation {
        // Get restaurant name
        val restaurant = RestaurantDataProvider.getRestaurantById(restaurantId)
        val restaurantName = restaurant?.name ?: "Unknown Restaurant"

        val reservation = Reservation(
            restaurantId = restaurantId,
            restaurantName = restaurantName,
            userEmail = userEmail,
            date = date,
            time = time,
            guests = guests,
            status = ReservationStatus.RESERVED
        )

        val reservations = getAllReservations().toMutableList()
        reservations.add(reservation)
        saveReservations(reservations)

        return reservation
    }

    // Update reservation status
    fun updateReservationStatus(reservationId: String, status: ReservationStatus) {
        val reservations = getAllReservations().toMutableList()
        val index = reservations.indexOfFirst { it.id == reservationId }

        if (index != -1) {
            val updated = reservations[index].copy(status = status)
            reservations[index] = updated
            saveReservations(reservations)
        }
    }

    // Get all reservations for a specific user
    fun getUserReservations(userEmail: String): List<Reservation> {
        return getAllReservations().filter { it.userEmail == userEmail }
    }

    // Get upcoming reservations (status = RESERVED)
    fun getUpcomingReservations(userEmail: String): List<Reservation> {
        return getUserReservations(userEmail)
            .filter { it.status == ReservationStatus.RESERVED }
            .sortedBy { getReservationDateTime(it) }
    }

    // Get past reservations (status = COMPLETED or CANCELLED)
    fun getPastReservations(userEmail: String): List<Reservation> {
        return getUserReservations(userEmail)
            .filter { it.status == ReservationStatus.COMPLETED || it.status == ReservationStatus.CANCELLED }
            .sortedByDescending { getReservationDateTime(it) }
    }

    // Helper method to parse reservation date and time into a Date object for sorting
    private fun getReservationDateTime(reservation: Reservation): Date {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault())
        return try {
            dateFormat.parse("${reservation.date} ${reservation.time}") ?: Date()
        } catch (e: Exception) {
            Date()
        }
    }

    // Helper method to get all reservations
    private fun getAllReservations(): List<Reservation> {
        val json = sharedPreferences.getString(KEY_RESERVATIONS, null) ?: return emptyList()
        val type: Type = object : TypeToken<List<Reservation>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Helper method to save all reservations
    private fun saveReservations(reservations: List<Reservation>) {
        val json = gson.toJson(reservations)
        editor.putString(KEY_RESERVATIONS, json)
        editor.apply()
    }

    // Fixed: Auto-update reservations status based on current time
    fun refreshReservationStatuses(userEmail: String) {
        val cal = Calendar.getInstance()
        val reservations = getAllReservations().toMutableList()
        var updated = false

        for (i in reservations.indices) {
            val reservation = reservations[i]
            if (reservation.userEmail != userEmail || reservation.status != ReservationStatus.RESERVED) {
                continue
            }

            val reservationDateTime = getReservationDateTime(reservation)
            val reservationCal = Calendar.getInstance()
            reservationCal.time = reservationDateTime

            // Add 2 hours to reservation time to avoid premature completion
//            reservationCal.add(Calendar.HOUR_OF_DAY, 2)

            // Check if the reservation time (plus 2 hours) is before current time
//            if (reservationCal.before(cal)) {
//                // Reservation date has passed, mark as completed
//                reservations[i] = reservation.copy(status = ReservationStatus.COMPLETED)
//                updated = true
//            }
        }

        if (updated) {
            saveReservations(reservations)
        }
    }
}