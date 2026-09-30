package com.example.ericac.ui.screens

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ericac.R
import com.example.ericac.model.Product
import com.example.ericac.repository.ProductRepository
import com.example.ericac.ui.theme.EricACTheme
import com.example.ericac.viewmodel.CartViewModel

@Composable
fun CatalogScreen(
    cartViewModel: CartViewModel = viewModel(),
    onNavigateToPayment: (Double) -> Unit,
    onActivateSdk: () -> Unit,
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalPrice = cartViewModel.getTotalPrice()

    val products = ProductRepository.products

    CatalogProducts(
        modifier = Modifier.fillMaxSize(),
        cartItens = cartItems,
        catalogOnIncrement = { product -> cartViewModel.increment(product) },
        catalogOnDecrement = { product -> cartViewModel.decrement(product) },
        onCheckoutClick = { onNavigateToPayment(totalPrice) },
        totalPrice = totalPrice,
        products = products,
        onActivateSdkClick = onActivateSdk,
        // NOVO PARÂMETRO REPASSADO
    )
}

@Composable
fun CatalogProducts(
    modifier: Modifier = Modifier,
    cartItens: Map<Product, Int>,
    catalogOnIncrement: (Product) -> Unit,
    catalogOnDecrement: (Product) -> Unit,
    onCheckoutClick: () -> Unit,
    totalPrice: Double,
    products: List<Product>,
    onActivateSdkClick: () -> Unit

) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Fully stable custom TopBar using Surface
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shadowElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .height(56.dp) // Standard TopBar height in Android
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "Eric Automação Comercial",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.combinedClickable(
                            onClick = { }, // Clique simples opcional
                            onLongClick = { onActivateSdkClick() } // Clique longo dispara a ativação
                        )
                    )
                }
            }
        },
        bottomBar = {
            CheckoutBar(total = totalPrice, onPayClick = onCheckoutClick)
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            items(products) { product ->
                val quantity = cartItens[product] ?: 0
                ProductCard(
                    product = product,
                    quantity = quantity,
                    onIncrement = { catalogOnIncrement(product) },
                    onDecrement = { catalogOnDecrement(product) }
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatalogScreenPreview() {
    val mockProducts = listOf(
        Product(id = 1, name = "Maçã", price = 5.0, image = R.drawable.apple),
        Product(id = 2, name = "Banana", price = 3.5, image = R.drawable.banana)
    )

    val mockCartItems = mapOf(
        mockProducts[0] to 2
    )

    EricACTheme() {
        CatalogProducts(
            modifier = Modifier.fillMaxSize(),
            cartItens = mockCartItems,
            catalogOnIncrement = {}, // 2 maçãs * 5.0
            catalogOnDecrement = {}, // Não faz nada no preview
            onCheckoutClick = {}, // Não faz nada no preview
            totalPrice = 10.0,
            products = mockProducts,
            onActivateSdkClick = {} // Não faz nada no Preview
        )
    }
}