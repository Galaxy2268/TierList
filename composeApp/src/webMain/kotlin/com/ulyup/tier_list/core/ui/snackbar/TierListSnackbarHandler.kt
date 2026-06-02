package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState

class TierListSnackbarHandler(
    val hostState: SnackbarHostState,
    private val genericErrorMessage: String,
) {

    suspend fun showMessage(text: String) {
        show(text)
    }

    suspend fun showError(text: String?) {
        show(text ?: genericErrorMessage)
    }

    private suspend fun show(text: String) {
        hostState.currentSnackbarData?.dismiss()
        hostState.showSnackbar(text)
    }
}
