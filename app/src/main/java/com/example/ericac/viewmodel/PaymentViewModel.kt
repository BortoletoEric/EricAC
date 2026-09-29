package com.example.ericac.viewmodel

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import com.example.ericac.payment.paykit
import com.linx.paykit.common.Callback
import com.linx.paykit.common.PaymentResult
import com.linx.paykit.common.parameter.PaymentParameters
import java.math.BigDecimal

class PaymentViewModel : ViewModel() {

    // O valor na documentação pede em centavos, então multiplicamos por 100
    fun payPixDeeplink(context: Context, total: Double) {
        val amountInCents = (total * 100).toInt()
        val externalId = "PEDIDO_${System.currentTimeMillis()}"

        // Monta a URL baseada na documentação fornecida
        val url = "paykit://payment?paymentType=pix&amount=$amountInCents&externalId=$externalId&callbackUrl=ericac://callback"

        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent) // Dispara o deeplink
    }

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