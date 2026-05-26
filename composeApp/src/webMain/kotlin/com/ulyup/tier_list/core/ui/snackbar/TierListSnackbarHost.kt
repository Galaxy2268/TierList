package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.theme.appColors

@Composable
fun TierListSnackbarHost(handler: TierListSnackbarHandler) {
    SnackbarHost(hostState = handler.hostState) { data ->
        Snackbar(
            snackbarData = data,
            containerColor = appColors.surface,
            contentColor = appColors.onSurface,
            actionColor = appColors.primary,
        )
    }
}