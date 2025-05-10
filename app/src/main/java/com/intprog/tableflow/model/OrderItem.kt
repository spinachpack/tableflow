package com.intprog.tableflow.model

import java.io.Serializable

data class OrderItem(
    val menuItem: MenuItem,
    var quantity: Int
) : Serializable
