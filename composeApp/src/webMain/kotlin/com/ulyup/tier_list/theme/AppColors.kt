package com.ulyup.tier_list.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.ulyup.tier_list.model.Tier

@Immutable
data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,

    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val onBackground: Color,
    val onSurface: Color,
    val onSurfaceVariant: Color,

    val outline: Color,
    val divider: Color,

    val error: Color,
    val onError: Color,
    val warning: Color,
    val success: Color,

    val tierS: Color,
    val tierA: Color,
    val tierB: Color,
    val tierC: Color,
    val tierD: Color,
    val tierF: Color,
) {
    val onTier: Color get() = background

    fun tierColor(tier: Tier): Color = when (tier) {
        Tier.S -> tierS
        Tier.A -> tierA
        Tier.B -> tierB
        Tier.C -> tierC
        Tier.D -> tierD
        Tier.F -> tierF
    }
}

internal val LocalAppColors = staticCompositionLocalOf { darkAppColors }

internal val darkAppColors = AppColors(
    primary = Color(0xFF7C5CFF),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFF00D4C9),
    onSecondary = Color(0xFF0F1115),

    background = Color(0xFF0F1115),
    surface = Color(0xFF1A1D24),
    surfaceVariant = Color(0xFF252932),
    onBackground = Color(0xFFE6E8EE),
    onSurface = Color(0xFFE6E8EE),
    onSurfaceVariant = Color(0xFF9BA1AE),

    outline = Color(0xFF3A3F4A),
    divider = Color(0xFF2A2E37),

    error = Color(0xFFFF5C7A),
    onError = Color(0xFFFFFFFF),
    warning = Color(0xFFFFB300),
    success = Color(0xFF4ADE80),

    tierS = Color(0xFFFF7F7F),
    tierA = Color(0xFFFFBF7F),
    tierB = Color(0xFFFFDF7F),
    tierC = Color(0xFFFFFF7F),
    tierD = Color(0xFFBFFF7F),
    tierF = Color(0xFF7FFFFF),
)

internal fun AppColors.toMaterialColorScheme(): ColorScheme = darkColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    secondary = secondary,
    onSecondary = onSecondary,
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    outline = outline,
    error = error,
    onError = onError,
)