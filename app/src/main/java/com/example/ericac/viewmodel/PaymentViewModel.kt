package com.example.ericac.viewmodel

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ericac.payment.paykit
import com.linx.paykit.common.Callback
import com.linx.paykit.common.CancelResult
import com.linx.paykit.common.PaymentResult
import com.linx.paykit.common.TransactionStatus
import com.linx.paykit.common.parameter.CancelParameter
import com.linx.paykit.common.parameter.StartPaymentParameters
import com.linx.paykit.common.printer.PrintResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.time.Duration.Companion.milliseconds

class PaymentViewModel : ViewModel() {
    private val isMockMode = false
    fun iniciarPagamentoWhiteLabel(total: Double, onResult: (Boolean, String, String?) -> Unit) {
        val parameters = StartPaymentParameters(
            amount = BigDecimal(total.toString()),
            autoConfirm = true,
            autoPrintReceipt = false
        )
        paykit?.startPayment(parameters) { result ->
            Log.i("PaymentResult", "ID: ${result.id} | Status: ${result.status}")

            viewModelScope.launch(Dispatchers.Main) {
                if (result.status == TransactionStatus.APPROVED || result.status == TransactionStatus.COMPLETED) {
                    // Retorna o result.id recebido do SDK!
                    onResult(true, "Pagamento aprovado com sucesso!", result.id)
                } else {
                    onResult(false, result.message ?: "Pagamento não concluído.", null)
                }
            }
        }
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
        paykit?.print(bitmap) { result ->
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
    }

    // Fluxo de cancelamento com suporte a Mock
    fun cancelarPagamento(
        transactionId: String,
        amount: Double,
        onResult: (Boolean, String) -> Unit
    ) {
        if (isMockMode) {
            viewModelScope.launch(Dispatchers.Main) {
                delay(1500) // Simula latência de rede/hardware
                Log.i("CancelResult", "Mock: Cancelamento aprovado para ID: $transactionId")
                onResult(true, "Mock: Pagamento cancelado com sucesso!")
            }
            return
        }

        // Montagem do parâmetro garantindo os requisitos da documentação (amount >= 0.01)
        val cancelParameter = CancelParameter(
            paymentId = transactionId,
            amount = BigDecimal(amount.toString()),
            autoPrintReceipt = true // Recomendado imprimir via do lojista no SmartPOS
        )

        paykit?.cancel(cancelParameter) { t ->
            Log.i("CancelResult", "ID: ${t.id} | Status: ${t.status}")

            // Redireciona o fluxo de volta para a Main Thread antes de devolver para o Compose
            viewModelScope.launch(Dispatchers.Main) {
                if (t.status == TransactionStatus.APPROVED || t.status == TransactionStatus.CANCELLED) {
                    onResult(true, t.message ?: "Cancelamento realizado com sucesso.")
                } else {
                    onResult(
                        false,
                        t.message ?: "Erro ao processar o cancelamento: ${t.status}"
                    )
                }
            }
        }
    }
}