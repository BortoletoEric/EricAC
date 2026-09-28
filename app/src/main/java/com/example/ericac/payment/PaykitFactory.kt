package com.example.ericac.payment

import android.content.Context
import android.util.Log
import com.linx.paykit.common.activation.ActivationParameters
import com.linx.paykit.common.builder.Parameters
import com.linx.paykit.common.builder.PaykitId
import com.linx.paykit.core.Paykit
import com.linx.paykit.core.PaykitFactory

val TAG = "SDKUnicoExample"
var paykit: Paykit? = null

// Passo 2: INICIALIZAÇÃO (Roda toda vez que o app abrir)
fun initPaykit(context: Context) {
    // Agora passamos o "context" que recebemos da MainActivity
    val sdkUnicoBuildParams = Parameters(context, "EricAC", PaykitId("SEU_PAYKIT_ID_AQUI"))
    paykit = PaykitFactory().build(sdkUnicoBuildParams)

    // Se precisar desativar métodos de pagamento, faça aqui:
    // paykit?.paymentMethods?.get(PaymentType.VOUCHER)?.enabled = false
}

// Passo 1: ATIVAÇÃO (Roda apenas na primeira vez)
fun activatePaykit() {
    val params = ActivationParameters("12839955000116").apply {}

    paykit?.activate(params) {
        Log.d(TAG, "SDK Ativado com sucesso")
        // Futuramente colocar SharedPreferences dizendo "ja_ativou = true"
    }
}