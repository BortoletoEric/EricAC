package com.example.ericac.viewmodel

import androidx.lifecycle.ViewModel
import com.example.ericac.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<Map<Product, Int>>(emptyMap())
    val cartItems: StateFlow<Map<Product, Int>> = _cartItems.asStateFlow()

    fun increment(product: Product) {
        val currentCart = _cartItems.value.toMutableMap()
        val currentQuantity = currentCart[product] ?: 0
        currentCart[product] = currentQuantity + 1
        _cartItems.value = currentCart
    }

    fun decrement(product: Product) {
        val currentCart = _cartItems.value.toMutableMap()
        val currentQuantity = currentCart[product] ?: 0

        if (currentQuantity > 0) {
            if (currentQuantity == 1) {
                currentCart.remove(product)
            } else {
                currentCart[product] = currentQuantity - 1
            }
            _cartItems.value = currentCart
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.entries.sumOf { (product, quantity) ->
            product.price * quantity
        }
    }
}