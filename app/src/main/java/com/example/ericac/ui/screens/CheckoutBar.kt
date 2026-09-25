package com.example.ericac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ericac.ui.components.PrimaryButton

@Composable
fun CheckoutBar() {
    Row(
        modifier = Modifier
            .background(Color.Black)
            .padding(16.dp)
            .padding(bottom = 4.dp)
            .navigationBarsPadding()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "R$:28,94",
            color = MaterialTheme.colorScheme.surface,
            textAlign = TextAlign.Center
        )
        PrimaryButton(
            modifier = Modifier.width(200.dp),
            text = "Pagar",
            onClick = {

            }
        )
    }
}

@Composable
@Preview
fun CheckoutBarPreview() {
    CheckoutBar()
}