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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal
import kotlin.time.Duration.Companion.milliseconds

// Exponha estados claros para a UI consumir
sealed class PaymentUiState {
    object Idle : PaymentUiState()
    object Processing : PaymentUiState()
    data class Success(val message: String) : PaymentUiState()
    data class Error(val message: String) : PaymentUiState()
    data class Rollback(val message: String) : PaymentUiState()
}

class PaymentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<PaymentUiState>(PaymentUiState.Idle)
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = PaymentUiState.Idle
    }

    // Único ponto de entrada para a transação completa
    fun processarVendaCompleta(total: Double, receiptBitmap: Bitmap) {
        _uiState.value = PaymentUiState.Processing

        val parameters = StartPaymentParameters(
            amount = BigDecimal(total.toString()),
            autoConfirm = true,
            autoPrintReceipt = false
        )

        paykit?.startPayment(parameters) { result ->
            if (result.status == TransactionStatus.APPROVED || result.status == TransactionStatus.COMPLETED) {
                val transactionId = result.id
                if (transactionId != null) {
                    // Pagamento aprovado, tenta imprimir
                    iniciarImpressaoSegura(transactionId, total, receiptBitmap)
                } else {
                    _uiState.value = PaymentUiState.Error("Pagamento aprovado, mas sem ID de transação para rastreio.")
                }
            } else {
                _uiState.value = PaymentUiState.Error(result.message ?: "Pagamento não concluído.")
            }
        }
    }

    private fun iniciarImpressaoSegura(transactionId: String, total: Double, bitmap: Bitmap) {
        paykit?.print(bitmap) { printResult ->
            if (printResult.success) {
                _uiState.value = PaymentUiState.Success("Venda concluída e impressa com sucesso!")
            } else {
                // Falha de impressão gera rollback obrigatório
                executarRollbackSeguranca(transactionId, total)
            }
        }
    }

    private fun executarRollbackSeguranca(transactionId: String, amount: Double) {
        val cancelParameter = CancelParameter(
            paymentId = transactionId,
            amount = BigDecimal(amount.toString()),
            autoPrintReceipt = true
        )

        paykit?.cancel(cancelParameter) { cancelResult ->
            if (cancelResult.status == TransactionStatus.APPROVED || cancelResult.status == TransactionStatus.CANCELLED) {
                _uiState.value = PaymentUiState.Rollback("Falha na impressão. Venda estornada por segurança.")
            } else {
                _uiState.value = PaymentUiState.Error("ERRO CRÍTICO: Falha na impressora e falha no estorno. Contate o suporte.")
            }
        }
    }
}