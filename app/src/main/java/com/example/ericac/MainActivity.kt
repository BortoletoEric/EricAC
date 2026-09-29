package com.example.ericac

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ericac.payment.buildPaykit
import com.example.ericac.ui.screens.CatalogScreen
import com.example.ericac.ui.theme.EricACTheme
import com.example.ericac.viewmodel.CartViewModel
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
            // 1. Instancia as ViewModels
            val paymentViewModel: PaymentViewModel = viewModel()
            val cartViewModel: CartViewModel = viewModel() // Para podermos limpar os itens depois

            // 2. Captura o contexto corretamente no Compose
            val context = LocalContext.current

            CatalogScreen(
                cartViewModel = cartViewModel, // Passa a referência para a tela
                onNavigateToPayment = { totalAmount ->

                    paymentViewModel.iniciarPagamentoWhiteLabel(totalAmount) { sucesso, msg ->
                        if (sucesso) {
                            // 3. Usa o 'context' capturado em vez de 'this'
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

                            // 4. Esvazia o carrinho após o pagamento aprovado
                            cartViewModel.clearCart()
                        } else {
                            // Bom também mostrar a mensagem se o usuário cancelar ou der erro
                            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            )
        }
    }
}