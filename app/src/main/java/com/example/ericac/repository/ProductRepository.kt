package com.example.ericac.repository

import com.example.ericac.R
import com.example.ericac.model.Product

object ProductRepository {
    val products = listOf(
        Product(
            id = 1,
            name = "Maça",
            image = R.drawable.apple,
            price = 3.00
        ),
        Product(
            id = 2,
            name = "Banana",
            image = R.drawable.banana,
            price = 2.00
        ),
        Product(
            id = 3,
            name = "Abacaxi",
            image = R.drawable.pinapple,
            price = 5.0
        ),
        Product(
            id = 4,
            name = "Kiwi",
            image = R.drawable.kiwi,
            price = 5.50
        )
    )
}