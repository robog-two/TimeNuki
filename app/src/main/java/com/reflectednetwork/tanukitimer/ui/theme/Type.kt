package com.reflectednetwork.tanukitimer.ui.theme

import android.graphics.fonts.SystemFonts
import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.sp
import com.reflectednetwork.tanukitimer.R

// Set of Material typography styles to start with
val Typography = Typography(
        body1 = TextStyle(
                fontFamily = FontFamily(Font(R.font.francoisone)),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp
        )
        /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)