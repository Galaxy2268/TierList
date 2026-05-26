package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTierListSnackbarHandler = staticCompositionLocalOf<TierListSnackbarHandler> {
    error("LocalTierListSnackbarHandler not provided")
}