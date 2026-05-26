package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.runtime.staticCompositionLocalOf

val LocalTierRankSnackbarHandler = staticCompositionLocalOf<TierRankSnackbarHandler> {
    error("LocalTierRankSnackbarHandler not provided")
}