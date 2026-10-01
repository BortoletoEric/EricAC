package com.example.ericac.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ericac.payment.paykit
import com.linx.paykit.common.Callback
import com.linx.paykit.common.PaymentResult
import com.linx.paykit.common.TransactionStatus
import com.linx.paykit.common.parameter.StartPaymentParameters
import com.linx.paykit.common.printer.PrintResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.time.Duration.Companion.milliseconds

class PaymentViewModel : ViewModel() {
    private val isMockMode = false
    fun iniciarPagamentoWhiteLabel(total: Double, onResult: (Boolean, String) -> Unit) {
        val parameters = StartPaymentParameters(
            amount = BigDecimal(total.toString()),
            autoConfirm = true,
            autoPrintReceipt = true
        )

        paykit?.startPayment(parameters, object : Callback<PaymentResult> {
            override fun execute(result: PaymentResult) {
                Log.i("PaymentResult", "ID: ${result.id} | Status: ${result.status}")

                // O SDK pode devolver o callback numa thread de background (Dispatchers.IO).
                // Usamos o viewModelScope para devolver a resposta para a tela do Jetpack Compose na Main Thread.
                viewModelScope.launch(Dispatchers.Main) {
                    // Substitua "result.success" pela propriedade exata de sucesso da classe PaymentResult
                    // (Pode ser algo como result.status == TransactionStatus.APPROVED)
                    if (result.status == TransactionStatus.APPROVED || result.status == TransactionStatus.COMPLETED) {
                        onResult(true, "Pagamento aprovado com sucesso!")
                    } else {
                        onResult(false, result.message ?: "Pagamento não concluído.")
                    }
                }
            }
        })
    }

    // Fluxo de impressão com suporte a Mock
    fun imprimirRecibo(bitmap: Bitmap, onResult: (Boolean, String) -> Unit) {
        if (isMockMode) {
            viewModelScope.launch(Dispatchers.Main) {
                delay(2000.milliseconds) // Simula o tempo que a impressora leva para ejetar o papel
                Log.i("PrintResult", "Mock: Impressão finalizada (SUCESSO)")
                onResult(true, "Mock: Impressão concluída com sucesso.")
            }
            return
        }

        // Fluxo real utilizando o SDK (Necessita importar PrintResult de PrintResult)
        paykit?.print(bitmap, object : Callback<PrintResult> {
            override fun execute(result: PrintResult) {
                Log.i("PrintResult", "Status: ${result.status}")
                viewModelScope.launch(Dispatchers.Main) {
                    // Verifique o Enum de sucesso (pode variar de acordo com o SDK, como PrintStatus.SUCCESS)
                    if (result.success) {
                        onResult(true, "Impressão concluída.")
                    } else {
                        onResult(false, result.message ?: "Erro ao imprimir: ${result.status}")
                    }
                }
            }
        })
    }

    // Fluxo de cancelamento com suporte a Mock
    fun cancelarPagamento(transactionId: String, onResult: (Boolean, String) -> Unit) {
        if (isMockMode) {
            viewModelScope.launch(Dispatchers.Main) {
                delay(1500)
                Log.i("CancelResult", "Mock: Cancelamento aprovado para ID: $transactionId")
                onResult(true, "Mock: Pagamento cancelado com sucesso!")
            }
            return
        }

        // TODO: Inserir a chamada real de cancelamento do Paykit aqui futuramente
        // paykit?.cancelPayment(...)
    }
}