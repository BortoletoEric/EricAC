package com.example.ericac.payment

import android.content.Context
import android.util.Log
import com.linx.paykit.common.activation.ActivationParameters
import com.linx.paykit.common.builder.Parameters
import com.linx.paykit.common.builder.PaykitId
import com.linx.paykit.core.Paykit
import com.linx.paykit.core.PaykitFactory
import androidx.core.content.edit
import com.linx.paykit.common.activation.TipoServidor
import com.linx.paykit.ui.data.PaykitUiModel

val TAG = "SDKUnicoPaykitFactory"
var paykit: Paykit? = null

fun buildPaykit(context: Context) {

// Inicializa o motor de interface do Paykit usando as telas internas (White Label)
    PaykitUiModel.build(
        context = context,
        callback = null
    )

    // constrói o paykit
    val sdkUnicoBuildParams = Parameters(context, "EricAC", PaykitId("HML-B256A8D9-42B9-45DA-AB63-D31FA5BEF748"))
    paykit = PaykitFactory().build(sdkUnicoBuildParams)

    // verifica se precisa ativar
    if (!verificarSeEstaAtivado(context)) {
        activatePaykit(context)
    } else {
        Log.d(TAG, "SDK já ativado")
    }
}
fun activatePaykit(context: Context) {
    lateinit var activationParameters: ActivationParameters

    activationParameters = ActivationParameters("12839955000116") // 1 por cliente
    activationParameters.tipoServidor = TipoServidor.LinxTef
    // Configurações Linxtef
    activationParameters.tef.production = false
    activationParameters.tef.token = "Rw\$b05;\$m2J6}Gq7wh@]" // 1 por ambiente (HML/PRD) - fornecido pela Linx

    paykit?.activate(activationParameters) { resultado ->
        // Só salva no SharedPreferences se a ativação realmente funcionou
        if (resultado.success) {
            Log.d(TAG, "SDK Ativado com sucesso")
            val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
            sharedPref.edit { putBoolean("isPaykitActivated", true) }
        } else {
            Log.e(TAG, "Falha ao ativar o SDK: ${resultado.message}")
        }
    }
}

fun verificarSeEstaAtivado(context: Context): Boolean {
    val sharedPref = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)

    // O segundo parâmetro (false) é o valor padrão caso a chave não exista
    return sharedPref.getBoolean("isPaykitActivated", false)
}