package com.example.ericac.model

import android.graphics.drawable.Icon
import android.media.Image
import androidx.annotation.DrawableRes

data class Product (
    val id: Int,
    val name: String,
    val price: Double,
    @DrawableRes val image: Int
)