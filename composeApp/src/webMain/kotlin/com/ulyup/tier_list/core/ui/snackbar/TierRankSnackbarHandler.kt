package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import org.jetbrains.compose.resources.getString

class TierRankSnackbarHandler(val hostState: SnackbarHostState) {

    suspend fun show(message: SnackbarMessage) {
        val text = when (message) {
            is SnackbarMessage.Plain -> message.text
            is SnackbarMessage.Resource -> getString(message.res)
        }
        hostState.showSnackbar(text)
    }
}