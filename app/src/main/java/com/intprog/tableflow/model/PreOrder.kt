package com.intprog.tableflow.model

import java.io.Serializable

data class PreOrder(
    val id: String,
    val reservationId: String,
    val items: MutableList<OrderItem>,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable