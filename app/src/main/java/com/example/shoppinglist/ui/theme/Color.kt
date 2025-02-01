package com.example.shoppinglist.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)


val lightpink = Color(0XFFEAD6EE)
val lightcyan = Color(0XFFA0F1EA)
val paleLavender = Color(0XFFD4B8F0)

val fill = arrayOf(
    0.2f to lightpink,
    0.5f to paleLavender,
    0.9f to lightcyan
)

val backBrush = Brush.linearGradient(colorStops = fill)