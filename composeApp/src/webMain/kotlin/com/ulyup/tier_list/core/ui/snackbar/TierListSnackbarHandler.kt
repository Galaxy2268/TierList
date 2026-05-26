package com.ulyup.tier_list.core.ui.snackbar

import androidx.compose.material3.SnackbarHostState
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.error_message
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

class TierListSnackbarHandler(val hostState: SnackbarHostState) {

    suspend fun showMessage(text: String) {
        hostState.showSnackbar(text)
    }

    suspend fun showMessage(res: StringResource) {
        hostState.showSnackbar(getString(res))
    }

    suspend fun showError(text: String?) {
        hostState.showSnackbar(text ?: getString(Res.string.error_message))
    }
}
