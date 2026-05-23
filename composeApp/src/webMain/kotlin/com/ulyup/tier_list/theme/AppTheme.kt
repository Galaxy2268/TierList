package com.ulyup.tier_list.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val colors = darkAppColors
    val typography = defaultAppTypography

    CompositionLocalProvider(
        LocalAppColors provides colors,
        LocalAppTypography provides typography,
    ) {
        MaterialTheme(
            colorScheme = colors.toMaterialColorScheme(),
            typography = typography.toMaterialTypography(),
            content = content,
        )
    }
}

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current

    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
}

val appColors: AppColors
    @Composable
    get() = AppTheme.colors

val appTypography: AppTypography
    @Composable
    get() = AppTheme.typography