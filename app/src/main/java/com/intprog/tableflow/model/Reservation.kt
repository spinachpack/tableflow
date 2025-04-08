package com.intprog.tableflow.model

import java.io.Serializable
import java.util.*

data class Reservation(
    val id: String = UUID.randomUUID().toString(),
    val restaurantId: String,
    val restaurantName: String,
    val userEmail: String,
    val date: String,
    val time: String,
    val guests: Int,
    val status: ReservationStatus,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

enum class ReservationStatus {
    RESERVED,
    COMPLETED,
    CANCELLED
}