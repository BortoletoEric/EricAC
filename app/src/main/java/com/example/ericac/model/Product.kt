package com.example.ericac.model

import android.graphics.drawable.Icon
import android.media.Image
import androidx.annotation.DrawableRes

data class Product (
    val name: String,
    @DrawableRes val image: Int
)