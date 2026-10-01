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
import com.example.ericac.utils.ReceiptHelper
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
            val cartViewModel: CartViewModel = viewModel()
            val context = LocalContext.current

            CatalogScreen(
                cartViewModel = cartViewModel, // Passa a referência para a tela
                onNavigateToPayment = { totalAmount ->
                    // INICIO PAGAMENTO
                    paymentViewModel.iniciarPagamentoWhiteLabel(totalAmount) { sucesso, msgPagamento, transactionId ->
                        if (sucesso && transactionId != null) {
                            Toast.makeText(context, msgPagamento, Toast.LENGTH_SHORT).show()

                            val receiptBitmap = ReceiptHelper.createMockReceiptBitmap(totalAmount)

                            //INICIO IMPRESSÃO
                            paymentViewModel.imprimirRecibo(receiptBitmap) { impressaoSucesso, msgImpressao ->
                                if (impressaoSucesso) {
                                    Toast.makeText(context, "Venda concluída e impressa!", Toast.LENGTH_LONG).show()
                                    cartViewModel.clearCart() // Sucesso total, limpa carrinho
                                } else {
                                    // ERRO IMPRESSÃO -> CUIDADO: Pagamento passou, mas impressora falhou. INICIAR ROLLBACK!
                                    Toast.makeText(context, "Falha na impressora. Iniciando estorno...", Toast.LENGTH_LONG).show()

                                    paymentViewModel.cancelarPagamento(transactionId, totalAmount) { cancelado, msgCancelamento ->
                                        if (cancelado) {
                                            Toast.makeText(context, "Venda estornada por falha de impressão.", Toast.LENGTH_LONG).show()
                                        } else {
                                            // ERRO CANCELAMENTO -> A pior situação possível. Cancelamento falhou. O lojista precisa cancelar manualmente no portal.
                                            Toast.makeText(context, "ERRO CRÍTICO: Não foi possível estornar. Contate o suporte.", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        } // ERRO PAGAMENTO
                        else {
                            Toast.makeText(context, msgPagamento, Toast.LENGTH_LONG).show()
                        }
                    }
                },
                onActivateSdk = {
                    buildPaykit(context)
                }
            )
        }
    }
}