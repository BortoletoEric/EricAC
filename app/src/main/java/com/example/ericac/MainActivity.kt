package com.example.ericac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ericac.model.PaymentTypes
import com.example.ericac.ui.screens.CatalogScreen
import com.example.ericac.ui.screens.PaymentScreen
import com.example.ericac.ui.theme.EricACTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        // Tela 1: Catálogo
        composable("catalog") {
            CatalogScreen(
                onNavigateToPayment = { totalAmount ->
                    // Navega para a tela de pagamento passando o valor total
                    navController.navigate("payment/$totalAmount")
                }
            )
        }

        // Tela 2: Pagamento (recebe o valor total como argumento)
        composable(
            route = "payment/{totalAmount}",
            arguments = listOf(navArgument("totalAmount") { type = NavType.FloatType })
        ) { backStackEntry ->
            val totalAmount = backStackEntry.arguments?.getFloat("totalAmount")?.toDouble() ?: 0.0

            PaymentScreen(
                total = totalAmount,
                onNavigateBack = { navController.popBackStack() },
                onPaymentSelected = { paymentType ->
                    // AQUI CHAMAREMOS O SDK!
                    processPayment(paymentType, totalAmount)
                }
            )
        }
    }
}

// Função temporária para simular o pagamento antes de ligar o SDK
fun processPayment(type: PaymentTypes, amount: Double) {
    println("Iniciando pagamento: $type de R$ $amount")
    // O próximo passo será colocar a lógica do SDK aqui!
}