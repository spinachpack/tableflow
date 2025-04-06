package com.intprog.tableflow.model

import com.intprog.tableflow.R

object RestaurantDataProvider {

    private val restaurants = listOf(
        Restaurant(
            id = "resto001",
            name = "House of Lechon",
            address = "Acacia Street",
            city = "Cebu City",
            zipCode = "6000",
            country = "Philippines",
            openingHours = "10:00 AM",
            closingHours = "9:00 PM",
            imageResource = R.drawable.lechon_image,
            description = "House of Lechon in Cebu is a must-visit for anyone craving authentic Filipino flavors, particularly its specialty—lechon (roast pig). With several branches around the city, this restaurant has become synonymous with one of Cebu's most beloved dishes. The setting is casual yet inviting, offering a relaxed atmosphere."
        ),
        Restaurant(
            id = "resto002",
            name = "STK Ta Bay",
            address = "1 Orchid Street",
            city = "Cebu City",
            zipCode = "6000",
            country = "Philippines",
            openingHours = "11:00 AM",
            closingHours = "10:00 PM",
            imageResource = R.drawable.stk_image,
            description = "STK ta Bay! in Cebu is a beloved spot for those looking to enjoy classic Filipino seafood and grilled dishes in a homey, heritage-inspired setting. Known for its flavorful sutukil (sugba, tula, kilaw), this family-run restaurant combines delicious Cebuano cooking with a cozy, antique-filled atmosphere. It's a great place to experience authentic local cuisine, especially with favorites like grilled tuna belly and kinilaw"
        ),
        Restaurant(
            id = "resto003",
            name = "Lighthouse Restaurant",
            address = "Gov. M. Cuenco Ave",
            city = "Cebu City",
            zipCode = "6000",
            country = "Philippines",
            openingHours = "10:00 AM",
            closingHours = "9:00 PM",
            imageResource = R.drawable.lighthouse_image,
            description = "Lighthouse Restaurant in Cebu offers classic Filipino dishes in an elegant, homey setting. Known for specialties like crispy pata and kare-kare, it’s a great place to enjoy traditional flavors with a touch of class, often accompanied by live local music."
        ),
        Restaurant(
            id = "resto004",
            name = "Tong Yang",
            address = "Lower Ground Floor, SM Seaside City",
            city = "Cebu City",
            zipCode = "6000",
            country = "Philippines",
            openingHours = "8:00 AM",
            closingHours = "9:00 PM",
            imageResource = R.drawable.tongyang_image,
            description = "Tong Yang is a go-to buffet spot for Korean BBQ and hotpot lovers. With a wide selection of meats, seafood, and sides, it’s perfect for those who enjoy grilling and cooking their own meals in a fun, interactive setting.\n" +
                    "\n"
        ),
    )

    fun getRestaurantById(id: String): Restaurant? {
        return restaurants.find { it.id == id }
    }
}
