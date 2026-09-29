package com.example.ericac

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ericac.model.PaymentTypes
import com.example.ericac.payment.buildPaykit
import com.example.ericac.ui.screens.CatalogScreen
import com.example.ericac.ui.screens.PaymentScreen
import com.example.ericac.ui.theme.EricACTheme
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
    // Como usamos singleTask no Manifest, o callback pode cair aqui se o app já estiver aberto
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleCallbackIntent(intent)
    }

    private fun handleCallbackIntent(intent: Intent?) {
        val data: Uri? = intent?.data

        // Verifica se o deeplink recebido é o nosso callback
        if (data != null && data.scheme == "ericac" && data.host == "callback") {
            val status = data.getQueryParameter("status") // APPROVED, DECLINED, etc.
            val transactionId = data.getQueryParameter("transactionId")
            val amount = data.getQueryParameter("amount")
            val message = data.getQueryParameter("message")

            Log.i("PagamentoRetorno", "Status: $status | ID: $transactionId | MSG: $message")

            // Aqui você pode atualizar um estado global, disparar uma navegação para
            // uma tela de "Pagamento Aprovado" ou mostrar um Toast.
            Toast.makeText(this, "Pagamento Aprovado", Toast.LENGTH_LONG).show()
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

            // Instancia a ViewModel do pagamento
            val paymentViewModel: PaymentViewModel = viewModel()
            val context = LocalContext.current
            PaymentScreen(
                total = totalAmount,
                onNavigateBack = { navController.popBackStack() },
                onPaymentSelected = { paymentType ->
                    // Verifica o tipo de pagamento selecionado e chama a função correspondente
                    when (paymentType) {
                        PaymentTypes.PIX -> paymentViewModel.payPixDeeplink(context, totalAmount)
                        PaymentTypes.CREDIT -> { /* Implementar depois */ }
                        PaymentTypes.DEBIT -> { /* Implementar depois */ }
                    }
                }
            )
        }
    }
}