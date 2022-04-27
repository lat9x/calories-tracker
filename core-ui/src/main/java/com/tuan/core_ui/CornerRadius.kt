package com.tuan.core_ui

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class CornerRadius(
    val default: Dp = 0.dp,
    val radiusSmall: Dp = 10.dp,
    val radiusMedium: Dp = 30.dp,
    val radiusLarge: Dp = 50.dp,
    val radiusExtraLarge: Dp = 100.dp
)

val LocalCornerRadius = compositionLocalOf { CornerRadius() }

