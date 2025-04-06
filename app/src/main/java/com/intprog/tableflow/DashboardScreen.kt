package com.intprog.tableflow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout

class DashboardScreen : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_screen)

        val restaurantBox : LinearLayout = findViewById(R.id.restaurantBox)
        val restaurantButton = findViewById<Button>(R.id.restaurantButton)
        val restaurantBox2 : LinearLayout = findViewById(R.id.restaurantBox2)
        val restaurantButton2 = findViewById<Button>(R.id.restaurantButton2)
        val restaurantBox3 : LinearLayout = findViewById(R.id.restaurantBox3)
        val restaurantButton3 = findViewById<Button>(R.id.restaurantButton3)
        val restaurantBox4 : LinearLayout = findViewById(R.id.restaurantBox4)
        val restaurantButton4 = findViewById<Button>(R.id.restaurantButton4)


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

    }
}