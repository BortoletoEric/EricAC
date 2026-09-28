package com.example.ericac.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ericac.model.PaymentTypes
import com.example.ericac.ui.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    total: Double,
    onNavigateBack: () -> Unit,
    onPaymentSelected: (PaymentTypes) -> Unit // Criaremos esse Enum abaixo
) {
    Scaffold(
        topBar = {
            // Uma TopBar simples com botão de voltar
            CenterAlignedTopAppBar(
                title = { Text("Pagamento") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Text("< Voltar") // Pode usar um Icon aqui depois
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("Total a pagar: R$ %.2f", total),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Botões para as opções de pagamento
            PrimaryButton(
                text = "Crédito",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                onClick = { onPaymentSelected(PaymentTypes.CREDIT) }
            )

            PrimaryButton(
                text = "Débito",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                onClick = { onPaymentSelected(PaymentTypes.DEBIT) }
            )

            PrimaryButton(
                text = "Pix",
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                onClick = { onPaymentSelected(PaymentTypes.PIX) }
            )
        }
    }
}