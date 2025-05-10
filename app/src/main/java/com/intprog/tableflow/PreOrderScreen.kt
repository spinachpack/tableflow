package com.intprog.tableflow

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.intprog.tableflow.model.MenuItem
import com.intprog.tableflow.model.MenuDataProvider
import com.intprog.tableflow.model.OrderItem
import com.intprog.tableflow.model.PreOrder
import com.intprog.tableflow.model.PreOrderManager
import com.intprog.tableflow.model.Reservation
import com.intprog.tableflow.model.ReservationManager
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

    // Restaurant-specific menu layouts
    private lateinit var houseOfLechonMenu: LinearLayout
    private lateinit var stkMenu: LinearLayout
    private lateinit var lighthouseMenu: LinearLayout
    private lateinit var tongYangMenu: LinearLayout

    private val selectedItems = mutableListOf<OrderItem>()
    private var existingPreOrder: PreOrder? = null

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

        // Load existing pre-order if any
        loadExistingPreOrder()

        // Load restaurant menu
        loadMenu()
    }

    private fun initializeViews() {
        // Restaurant info
        restaurantNameText = findViewById(R.id.restaurantName)
        reservationDetails = findViewById(R.id.reservationDetails)
        totalPrice = findViewById(R.id.totalPrice)

        // Restaurant-specific menus
        houseOfLechonMenu = findViewById(R.id.houseOfLechonMenu)
        stkMenu = findViewById(R.id.stkMenu)
        lighthouseMenu = findViewById(R.id.lighthouseMenu)
        tongYangMenu = findViewById(R.id.tongYangMenu)

        // Back button
        findViewById<ImageView>(R.id.backButton).setOnClickListener {
            finish()
        }

        // Submit pre-order button
        findViewById<Button>(R.id.submitPreOrderButton).setOnClickListener {
            savePreOrder()
        }

        // Navigation buttons
        findViewById<View>(R.id.homeButton).setOnClickListener {
            // Navigate to home
            finish()
        }

        findViewById<View>(R.id.notificationButton).setOnClickListener {
            // Navigate to notifications
        }

        findViewById<View>(R.id.historyButton).setOnClickListener {
            // Already on history screen
        }

        findViewById<View>(R.id.moreButton).setOnClickListener {
            // Navigate to more options
        }
    }

    private fun loadExistingPreOrder() {
        existingPreOrder = preOrderManager.getPreOrderForReservation(reservation.id)

        if (existingPreOrder != null) {
            // Add items from existing pre-order to selected items
            selectedItems.addAll(existingPreOrder!!.items)
        }
    }

    private fun loadMenu() {
        // Set restaurant name
        restaurantNameText.text = reservation.restaurantName

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

        // Show specific menu based on restaurant ID
        when (reservation.restaurantId) {
            "house_of_lechon" -> {
                houseOfLechonMenu.visibility = View.VISIBLE
                setupLechonMenu()
            }
            "stk_ta_bay" -> {
                stkMenu.visibility = View.VISIBLE
                setupStkMenu()
            }
            "lighthouse" -> {
                lighthouseMenu.visibility = View.VISIBLE
                setupLighthouseMenu()
            }
            "tong_yang" -> {
                tongYangMenu.visibility = View.VISIBLE
                setupTongYangMenu()
            }
            else -> {
                // If restaurant ID doesn't match any predefined menus, show message
                Toast.makeText(this, "Menu not available for this restaurant", Toast.LENGTH_SHORT).show()
            }
        }

        updateTotalPrice()
    }

    private fun setupLechonMenu() {
        // Setup click listeners and quantity controls for each menu item
        setupMenuItem(findViewById(R.id.lechon_item1), "Whole Lechon", 3500.0)
        setupMenuItem(findViewById(R.id.lechon_item2), "Half Lechon", 1800.0)
        setupMenuItem(findViewById(R.id.lechon_item3), "Lechon Kawali", 350.0)
        setupMenuItem(findViewById(R.id.lechon_side1), "Rice Platter", 180.0)
        setupMenuItem(findViewById(R.id.lechon_side2), "Puso (Hanging Rice)", 20.0)
    }

    private fun setupStkMenu() {
        setupMenuItem(findViewById(R.id.stk_item1), "Sutukil Combo", 650.0)
        setupMenuItem(findViewById(R.id.stk_item2), "Grilled Fish", 350.0)
        setupMenuItem(findViewById(R.id.stk_item3), "Shrimp Platter", 450.0)
        setupMenuItem(findViewById(R.id.stk_side1), "Garlic Rice", 60.0)
        setupMenuItem(findViewById(R.id.stk_side2), "Bam-i Noodles", 120.0)
    }

    private fun setupLighthouseMenu() {
        setupMenuItem(findViewById(R.id.lighthouse_item1), "Crispy Pata", 550.0)
        setupMenuItem(findViewById(R.id.lighthouse_item2), "Kare-Kare", 450.0)
        setupMenuItem(findViewById(R.id.lighthouse_item3), "Sinigang na Hipon", 380.0)
        setupMenuItem(findViewById(R.id.lighthouse_dessert1), "Halo-Halo", 150.0)
        setupMenuItem(findViewById(R.id.lighthouse_dessert2), "Leche Flan", 120.0)
    }

    private fun setupTongYangMenu() {
        setupMenuItem(findViewById(R.id.tongyang_item1), "Korean BBQ Set A", 499.0)
        setupMenuItem(findViewById(R.id.tongyang_item2), "Korean BBQ Set B", 699.0)
        setupMenuItem(findViewById(R.id.tongyang_item3), "Samgyeopsal", 350.0)
        setupMenuItem(findViewById(R.id.tongyang_side1), "Kimchi", 80.0)
        setupMenuItem(findViewById(R.id.tongyang_side2), "Doenjang Jjigae", 120.0)
    }

    private fun setupMenuItem(itemView: View, name: String, price: Double) {
        // Find the views within the included layout
        val itemName = itemView.findViewById<TextView>(R.id.itemName)
        val itemPrice = itemView.findViewById<TextView>(R.id.itemPrice)
        val itemCount = itemView.findViewById<TextView>(R.id.itemCount)
        val decreaseButton = itemView.findViewById<ImageView>(R.id.decreaseItem)
        val increaseButton = itemView.findViewById<ImageView>(R.id.increaseItem)

        // Set the item details
        itemName.text = name

        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        itemPrice.text = formatter.format(price)

        // Create a menu item
        val menuItem = MenuItem(
            id = name.replace(" ", "_").toLowerCase(),
            name = name,
            price = price,
            description = "",
            restaurantId = reservation.restaurantId
        )

        // Check if this item exists in selected items
        val existingItem = selectedItems.find { it.menuItem.id == menuItem.id }
        var quantity = existingItem?.quantity ?: 0
        itemCount.text = quantity.toString()

        // Set up listeners
        decreaseButton.setOnClickListener {
            if (quantity > 0) {
                quantity--
                itemCount.text = quantity.toString()
                updateOrderItem(menuItem, quantity)
            }
        }

        increaseButton.setOnClickListener {
            quantity++
            itemCount.text = quantity.toString()
            updateOrderItem(menuItem, quantity)
        }
    }

    private fun updateOrderItem(menuItem: MenuItem, quantity: Int) {
        val existingItemIndex = selectedItems.indexOfFirst { it.menuItem.id == menuItem.id }

        if (existingItemIndex != -1) {
            if (quantity > 0) {
                // Update quantity
                selectedItems[existingItemIndex].quantity = quantity
            } else {
                // Remove item if quantity is 0
                selectedItems.removeAt(existingItemIndex)
            }
        } else if (quantity > 0) {
            // Add new item
            selectedItems.add(OrderItem(menuItem, quantity))
        }

        updateTotalPrice()
    }

    private fun updateTotalPrice() {
        val totalAmount = selectedItems.sumOf { it.menuItem.price * it.quantity }
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "PH"))
        totalPrice.text = "Total: ${formatter.format(totalAmount)}"
    }

    private fun savePreOrder() {
        // Filter items with quantity > 0
        val itemsToSave = selectedItems.filter { it.quantity > 0 }

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