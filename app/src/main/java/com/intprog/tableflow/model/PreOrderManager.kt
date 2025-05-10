package com.intprog.tableflow.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.UUID

class PreOrderManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PreOrderPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    companion object {
        private const val KEY_PRE_ORDERS = "pre_orders"
    }

    // Create a new pre-order for a reservation
    fun createPreOrder(reservationId: String, items: List<OrderItem>): PreOrder {
        val preOrder = PreOrder(
            id = UUID.randomUUID().toString(),
            reservationId = reservationId,
            items = items.toMutableList()
        )

        val preOrders = getAllPreOrders().toMutableList()

        // Remove existing pre-order for this reservation if any
        preOrders.removeIf { it.reservationId == reservationId }

        // Add the new pre-order
        preOrders.add(preOrder)
        savePreOrders(preOrders)

        return preOrder
    }

    // Get pre-order for a specific reservation
    fun getPreOrderForReservation(reservationId: String): PreOrder? {
        return getAllPreOrders().find { it.reservationId == reservationId }
    }

    // Update an existing pre-order
    fun updatePreOrder(preOrder: PreOrder) {
        val preOrders = getAllPreOrders().toMutableList()
        val index = preOrders.indexOfFirst { it.id == preOrder.id }

        if (index != -1) {
            preOrders[index] = preOrder
            savePreOrders(preOrders)
        }
    }

    // Delete a pre-order
    fun deletePreOrder(preOrderId: String) {
        val preOrders = getAllPreOrders().toMutableList()
        preOrders.removeIf { it.id == preOrderId }
        savePreOrders(preOrders)
    }

    // Calculate total price of a pre-order
    fun calculateTotalPrice(preOrder: PreOrder): Double {
        return preOrder.items.sumOf { it.menuItem.price * it.quantity }
    }

    // Helper method to get all pre-orders
    private fun getAllPreOrders(): List<PreOrder> {
        val json = sharedPreferences.getString(KEY_PRE_ORDERS, null) ?: return emptyList()
        val type: Type = object : TypeToken<List<PreOrder>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Helper method to save all pre-orders
    private fun savePreOrders(preOrders: List<PreOrder>) {
        val json = gson.toJson(preOrders)
        editor.putString(KEY_PRE_ORDERS, json)
        editor.apply()
    }
}