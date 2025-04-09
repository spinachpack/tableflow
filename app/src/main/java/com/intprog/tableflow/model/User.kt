package com.intprog.tableflow.model

import com.intprog.tableflow.R

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val password: String,
    val profilePictureId: Int = R.drawable.pfphehe // Default profile picture
)