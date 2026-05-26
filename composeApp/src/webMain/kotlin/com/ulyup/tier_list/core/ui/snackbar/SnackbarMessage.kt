package com.ulyup.tier_list.core.ui.snackbar

import org.jetbrains.compose.resources.StringResource

sealed interface SnackbarMessage {
    data class Plain(val text: String) : SnackbarMessage
    data class Resource(val res: StringResource) : SnackbarMessage
}