package com.example.ericac.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.ericac.payment.paykit
import com.linx.paykit.common.Callback
import com.linx.paykit.common.PaymentResult
import com.linx.paykit.common.parameter.PaymentParameters
import java.math.BigDecimal

class PaymentViewModel : ViewModel() {

    fun payPix(total: Double) {
        val paymentParams = PaymentParameters(
            amount = BigDecimal(total.toString()),
            externalId = "PEDIDO_${System.currentTimeMillis()}" // Um ID usando o horário da transação
        )

        paykit?.pix(paymentParams, object : Callback<PaymentResult> {
            override fun execute(t: PaymentResult) {
                Log.i("PaymentResult", "ID: ${t.id}, Transaction: ${t.rawData}")
            }

        })
    }
}