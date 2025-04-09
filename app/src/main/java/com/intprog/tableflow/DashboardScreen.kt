package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ImageView
import com.intprog.tableflow.model.SessionManager

class DashboardScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_screen)

        // Get references to UI elements
        val restaurantBox : LinearLayout = findViewById(R.id.restaurantBox)
        val restaurantButton = findViewById<Button>(R.id.restaurantButton)
        val restaurantBox2 : LinearLayout = findViewById(R.id.restaurantBox2)
        val restaurantButton2 = findViewById<Button>(R.id.restaurantButton2)
        val restaurantBox3 : LinearLayout = findViewById(R.id.restaurantBox3)
        val restaurantButton3 = findViewById<Button>(R.id.restaurantButton3)
        val restaurantBox4 : LinearLayout = findViewById(R.id.restaurantBox4)
        val restaurantButton4 = findViewById<Button>(R.id.restaurantButton4)

        val profileImage: ImageView = findViewById(R.id.profileImage)

        // Load the user's current profile image
        val sessionManager = SessionManager(this)
        val currentUser = sessionManager.getUserDetails()
        profileImage.setImageResource(currentUser.profilePictureId)

        restaurantBox.setOnClickListener{
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto001")
            startActivity(intent)
        }
        restaurantButton.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto001")
            startActivity(intent)
        }

        restaurantBox2.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto002")
            startActivity(intent)
        }
        restaurantButton2.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto002")
            startActivity(intent)
        }

        restaurantBox3.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto003")
            startActivity(intent)
        }
        restaurantButton3.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto003")
            startActivity(intent)
        }

        restaurantBox4.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto004")
            startActivity(intent)
        }
        restaurantButton4.setOnClickListener {
            val intent = Intent(this, RestaurantDetailScreen::class.java)
            intent.putExtra("RESTAURANT_ID", "resto004")
            startActivity(intent)
        }

        profileImage.setOnClickListener {
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
        }

        val homeButton : LinearLayout = findViewById(R.id.homeButton)
        val notificationButton : LinearLayout = findViewById(R.id.notificationButton)
        val moreButton : LinearLayout = findViewById(R.id.moreButton)
        val historyButton : LinearLayout = findViewById(R.id.historyButton)

        homeButton.setOnClickListener{

        }
        notificationButton.setOnClickListener{
            val intent = Intent(this, NotificationScreen::class.java)
            startActivity(intent)
        }
        moreButton.setOnClickListener{
            val intent = Intent(this, ProfileScreen::class.java)
            startActivity(intent)
        }
        historyButton.setOnClickListener{
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }
    }

    // Refresh profile image when returning to this activity
    override fun onResume() {
        super.onResume()

        val profileImage: ImageView = findViewById(R.id.profileImage)
        val sessionManager = SessionManager(this)
        val currentUser = sessionManager.getUserDetails()
        profileImage.setImageResource(currentUser.profilePictureId)
    }
}