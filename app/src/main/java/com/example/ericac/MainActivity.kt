package com.example.ericac

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ericac.payment.buildPaykit
import com.example.ericac.ui.screens.CatalogScreen
import com.example.ericac.ui.theme.EricACTheme
import com.example.ericac.utils.ReceiptHelper
import com.example.ericac.viewmodel.CartViewModel
import com.example.ericac.viewmodel.PaymentUiState
import com.example.ericac.viewmodel.PaymentViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildPaykit(this)
        enableEdgeToEdge()

        setContent {
            EricACTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "catalog") {
        composable("catalog") {
            val paymentViewModel: PaymentViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel()
            val context = LocalContext.current

            // Observa o estado da ViewModel
            val paymentState by paymentViewModel.uiState.collectAsState()

            // Reage às mudanças de estado
            LaunchedEffect(paymentState) {
                when (paymentState) {
                    is PaymentUiState.Success -> {
                        Toast.makeText(context, (paymentState as PaymentUiState.Success).message, Toast.LENGTH_LONG).show()
                        cartViewModel.clearCart() // Limpa apenas após sucesso absoluto validado pela VM
                        paymentViewModel.resetState()
                    }
                    is PaymentUiState.Rollback -> {
                        Toast.makeText(context, (paymentState as PaymentUiState.Rollback).message, Toast.LENGTH_LONG).show()
                        paymentViewModel.resetState()
                    }
                    is PaymentUiState.Error -> {
                        Toast.makeText(context, (paymentState as PaymentUiState.Error).message, Toast.LENGTH_LONG).show()
                        paymentViewModel.resetState()
                    }
                    else -> {}
                }
            }

            CatalogScreen(
                cartViewModel = cartViewModel,
                onNavigateToPayment = { totalAmount ->
                    // A UI passa a intenção e os dados brutos. A VM resolve o resto.
                    val receiptBitmap = ReceiptHelper.createMockReceiptBitmap(totalAmount)
                    paymentViewModel.processarVendaCompleta(totalAmount, receiptBitmap)
                },
                onActivateSdk = { buildPaykit(context) }
            )
        }
    }
}