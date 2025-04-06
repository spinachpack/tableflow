package com.intprog.tableflow.model

data class Restaurant(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val zipCode: String,
    val country: String,
    val openingHours: String,
    val closingHours: String,
    val imageResource: Int,
    val description: String
)