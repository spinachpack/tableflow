package com.intprog.tableflow

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.MenuAdapter
import com.intprog.tableflow.model.MenuItem
import com.intprog.tableflow.model.MenuDataProvider
import com.intprog.tableflow.model.OrderItem
import com.intprog.tableflow.model.PreOrder
import com.intprog.tableflow.model.PreOrderManager
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationManager
import com.intprog.tableflow.model.RestaurantDataProvider
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class PreOrderScreen : Activity() {

    private lateinit var reservationManager: ReservationManager
    private lateinit var preOrderManager: PreOrderManager
    private lateinit var reservation: Reservation

    private lateinit var restaurantNameText: TextView
    private lateinit var reservationDetails: TextView
    private lateinit var totalPrice: TextView
    private lateinit var menuListView: ListView

    // Single source of truth for menu items
    private val orderItems = mutableListOf<OrderItem>()
    private var existingPreOrder: PreOrder? = null
    private lateinit var menuAdapter: MenuAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_order_screen)

        // Initialize managers
        reservationManager = ReservationManager(this)
        preOrderManager = PreOrderManager(this)

        // Get reservation ID from intent with improved error handling
        val reservationId = intent.getStringExtra("reservation_id")
        if (reservationId == null) {
            Toast.makeText(this, "Error: Reservation ID is missing", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Get reservation details with improved error handling
        val res = reservationManager.getReservationById(reservationId)
        if (res == null) {
            Toast.makeText(this, "Error: Reservation not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Assign reservation only after null check
        reservation = res

        // Initialize UI elements
        initializeViews()

        // Load menu first to initialize all menu items
        loadMenu()

        // Load existing pre-order if any
        loadExistingPreOrder()
    }

    private fun initializeViews() {
        // Restaurant info
        restaurantNameText = findViewById(R.id.restaurantName)
        reservationDetails = findViewById(R.id.reservationDetails)
        totalPrice = findViewById(R.id.totalPrice)

        // Menu list view
        menuListView = findViewById(R.id.menuListView)

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Submit pre-order button
        findViewById<Button>(R.id.submitPreOrderButton).setOnClickListener {
            savePreOrder()
        }

    }

    private fun loadMenu() {
        // Set restaurant name
        val restaurant = RestaurantDataProvider.getRestaurantById(reservation.restaurantId)
        restaurantNameText.text = restaurant?.name ?: reservation.restaurantName

        // Format reservation details
        val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

        // Parse date and time
        try {
            val sdfInput = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = sdfInput.parse(reservation.date)

            val sdfTimeInput = SimpleDateFormat("HH:mm", Locale.getDefault())
            val time = sdfTimeInput.parse(reservation.time)

            val formattedDate = if (date != null) dateFormat.format(date) else reservation.date
            val formattedTime = if (time != null) timeFormat.format(time) else reservation.time

            reservationDetails.text = "Reservation: $formattedDate at $formattedTime"
        } catch (e: Exception) {
            reservationDetails.text = "Reservation: ${reservation.date} at ${reservation.time}"
        }

        // Get menu items from provider
        val menuItems = MenuDataProvider.getMenuForRestaurant(reservation.restaurantId)

        // Initialize all menu items with quantity 0
        orderItems.clear()
        menuItems.forEach { menuItem ->
            orderItems.add(OrderItem(menuItem, 0))
        }

        // Setup adapter for menu list with the single source of truth
        menuAdapter = MenuAdapter(this, orderItems) { menuItem, newQuantity ->
            updateOrderItem(menuItem, newQuantity)
        }

        menuListView.adapter = menuAdapter
        updateTotalPrice()
    }

    private fun loadExistingPreOrder() {
        existingPreOrder = preOrderManager.getPreOrderForReservation(reservation.id)

        if (existingPreOrder != null) {
            // Update quantities for existing items
            existingPreOrder!!.items.forEach { existingItem ->
                val orderItemIndex = orderItems.indexOfFirst { it.menuItem.id == existingItem.menuItem.id }
                if (orderItemIndex != -1) {
                    orderItems[orderItemIndex].quantity = existingItem.quantity
                }
            }

            // Notify adapter of changes
            menuAdapter.notifyDataSetChanged()
            updateTotalPrice()
        }
    }

    private fun updateOrderItem(menuItem: MenuItem, quantity: Int) {
        // Find and update the item in our single source of truth
        val index = orderItems.indexOfFirst { it.menuItem.id == menuItem.id }
        if (index != -1) {
            orderItems[index].quantity = quantity

            // Important: Notify adapter that data has changed
            menuAdapter.notifyDataSetChanged()

            updateTotalPrice()
        }
    }

    private fun updateTotalPrice() {
        val totalAmount = orderItems.sumOf { it.menuItem.price * it.quantity }
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        totalPrice.text = "Total: ${formatter.format(totalAmount)}"
    }

    private fun savePreOrder() {
        // Filter items with quantity > 0
        val itemsToSave = orderItems.filter { it.quantity > 0 }

        if (itemsToSave.isEmpty()) {
            // If no items selected, delete existing pre-order if any
            if (existingPreOrder != null) {
                preOrderManager.deletePreOrder(existingPreOrder!!.id)
                Toast.makeText(this, "Pre-order deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No items selected", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Create or update pre-order
            preOrderManager.createPreOrder(reservation.id, itemsToSave)
            Toast.makeText(this, "Pre-order saved successfully", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}