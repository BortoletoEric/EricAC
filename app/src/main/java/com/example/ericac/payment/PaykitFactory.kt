package com.example.ericac.payment

import android.content.Context
import android.util.Log
import com.linx.paykit.common.activation.ActivationParameters
import com.linx.paykit.common.builder.Parameters
import com.linx.paykit.common.builder.PaykitId
import com.linx.paykit.core.Paykit
import com.linx.paykit.core.PaykitFactory
import androidx.core.content.edit

val TAG = "SDKUnicoPaykitFactory"
var paykit: Paykit? = null

fun buildPaykit(context: Context) {

    // constrói o paykit
    val sdkUnicoBuildParams = Parameters(context, "EricAC", PaykitId("example123"))
    paykit = PaykitFactory().build(sdkUnicoBuildParams)

    // verifica se precisa ativar
    if (!verificarSeEstaAtivado(context)) {
        activatePaykit(context)
    } else {
        Log.d(TAG, "SDK já ativado")
    }
}
fun activatePaykit(context: Context) {
    val params = ActivationParameters("12839955000116").apply {}

    paykit?.activate(params) {
        Log.d(TAG, "SDK Ativado com sucesso")
        val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPref.edit { putBoolean("isPaykitActivated", true) }
    }
}

fun verificarSeEstaAtivado(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    // O segundo parâmetro (false) é o valor padrão caso a chave não exista
    return sharedPref.getBoolean("isPaykitActivated", false)
}