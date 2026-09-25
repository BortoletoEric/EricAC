package com.example.ericac

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ericac.ui.screens.CatalogScreen
import com.example.ericac.ui.theme.EricACTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EricACTheme {
                CatalogScreen()
            }
        }
    }
}