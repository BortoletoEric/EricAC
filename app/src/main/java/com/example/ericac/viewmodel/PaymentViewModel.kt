package com.example.ericac.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ericac.payment.paykit
import com.linx.paykit.common.Callback
import com.linx.paykit.common.PaymentResult
import com.linx.paykit.common.TransactionStatus
import com.linx.paykit.common.parameter.PaymentParameters
import com.linx.paykit.common.parameter.StartPaymentParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal

class PaymentViewModel : ViewModel() {

    fun iniciarPagamentoWhiteLabel(total: Double, onResult: (Boolean, String) -> Unit) {
        val parameters = StartPaymentParameters(
            amount = BigDecimal(total.toString()),
            autoConfirm = true,
            autoPrintReceipt = true
        )

        paykit?.startPayment(parameters, object : Callback<PaymentResult> {
            override fun execute(result: PaymentResult) {
                Log.i("PaymentResult", "ID: ${result.id} | Status: ${result.status}")

                // O SDK pode devolver o callback em uma thread de background (Dispatchers.IO).
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
}