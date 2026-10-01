package com.example.ericac.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object ReceiptHelper {
    fun createMockReceiptBitmap(totalPrice: Double): Bitmap {
        // Padrão de largura de bobina térmica comum (ex: 384px ou 576px)
        val width = 384
        val height = 400
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Fundo branco
        canvas.drawColor(Color.WHITE)

        // Configuração da caneta preta
        val paint = Paint().apply {
            color = Color.BLACK
            textSize = 24f
            isAntiAlias = true
        }

        val titlePaint = Paint(paint).apply {
            textSize = 32f
            isFakeBoldText = true
        }

        // Escrevendo os textos do recibo
        canvas.drawText("ERIC AUTOMAÇÃO", 50f, 60f, titlePaint)
        canvas.drawText("---------------------------------", 10f, 100f, paint)
        canvas.drawText("CUPOM FISCAL MOCK", 50f, 140f, paint)
        canvas.drawText("Item 1: .................. R$ 0,00", 20f, 200f, paint)
        canvas.drawText("TOTAL: ................... R$ ${String.format("%.2f", totalPrice)}", 20f, 240f, titlePaint)
        canvas.drawText("---------------------------------", 10f, 300f, paint)
        canvas.drawText("Obrigado pela compra!", 50f, 340f, paint)

        return bitmap
    }
}