package com.intprog.tableflow.model

import java.io.Serializable

data class MenuItem(
    val id: String,
    val restaurantId: String,
    val name: String,
    val description: String,
    val price: Double,
    val imageResource: Int = 0
) : Serializable