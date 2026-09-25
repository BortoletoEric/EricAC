package com.example.ericac.repository

import com.example.ericac.R
import com.example.ericac.model.Product

object ProductRepository {
    val products = listOf(
        Product(
            name = "Maça",
            image = R.drawable.apple
        ),
        Product(
            name = "Banana",
            image = R.drawable.banana
        ),
        Product(
            name = "Abacaxi",
            image = R.drawable.pinapple
        ),
        Product(
            name = "Kiwi",
            image = R.drawable.kiwi
        )
    )
}