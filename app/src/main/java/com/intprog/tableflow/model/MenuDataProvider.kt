package com.intprog.tableflow.model

import com.intprog.tableflow.R
import java.util.UUID

object MenuDataProvider {

    // Menu items grouped by restaurant
    private val menuItems = mapOf(
        "resto001" to listOf(
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto001",
                name = "Original Cebu Lechon",
                description = "Whole roasted pig with crispy skin and flavorful meat",
                price = 650.00,
                imageResource = R.drawable.lechon_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto001",
                name = "Lechon Paksiw",
                description = "Leftover lechon meat cooked in a sweet and tangy sauce",
                price = 220.00,
                imageResource = R.drawable.lechon_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto001",
                name = "Puso (Hanging Rice)",
                description = "Traditional Cebuano rice wrapped in coconut leaves",
                price = 25.00,
                imageResource = R.drawable.lechon_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto001",
                name = "Bam-i",
                description = "Mixed noodle dish with meat and vegetables",
                price = 180.00,
                imageResource = R.drawable.lechon_image
            )
        ),
        "resto002" to listOf(
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto002",
                name = "Sinugba na Isda",
                description = "Grilled fish marinated in local spices",
                price = 280.00,
                imageResource = R.drawable.stk_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto002",
                name = "Kinilaw na Tanigue",
                description = "Fresh fish ceviche with vinegar and spices",
                price = 250.00,
                imageResource = R.drawable.stk_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto002",
                name = "Tinola na Isda",
                description = "Fish soup with ginger and green papaya",
                price = 220.00,
                imageResource = R.drawable.stk_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto002",
                name = "Grilled Squid",
                description = "Fresh squid marinated and grilled to perfection",
                price = 300.00,
                imageResource = R.drawable.stk_image
            )
        ),
        "resto003" to listOf(
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto003",
                name = "Crispy Pata",
                description = "Deep-fried pork knuckle with soy-vinegar dip",
                price = 495.00,
                imageResource = R.drawable.lighthouse_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto003",
                name = "Kare-Kare",
                description = "Oxtail stew with peanut sauce and vegetables",
                price = 380.00,
                imageResource = R.drawable.lighthouse_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto003",
                name = "Sizzling Sisig",
                description = "Chopped pig head parts served on a sizzling plate",
                price = 250.00,
                imageResource = R.drawable.lighthouse_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto003",
                name = "Pancit Canton",
                description = "Stir-fried egg noodles with meat and vegetables",
                price = 220.00,
                imageResource = R.drawable.lighthouse_image
            )
        ),
        "resto004" to listOf(
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto004",
                name = "Beef BBQ Set",
                description = "Assorted beef cuts for grilling",
                price = 420.00,
                imageResource = R.drawable.tongyang_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto004",
                name = "Seafood Set",
                description = "Assorted seafood for grilling or hotpot",
                price = 480.00,
                imageResource = R.drawable.tongyang_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto004",
                name = "Pork BBQ Set",
                description = "Assorted pork cuts for grilling",
                price = 380.00,
                imageResource = R.drawable.tongyang_image
            ),
            MenuItem(
                id = UUID.randomUUID().toString(),
                restaurantId = "resto004",
                name = "Vegetable Set",
                description = "Fresh vegetables for hotpot or side dishes",
                price = 180.00,
                imageResource = R.drawable.tongyang_image
            )
        )
    )

    fun getMenuForRestaurant(restaurantId: String): List<MenuItem> {
        return menuItems[restaurantId] ?: emptyList()
    }

    fun getMenuItemById(itemId: String): MenuItem? {
        return menuItems.values.flatten().find { it.id == itemId }
    }
}