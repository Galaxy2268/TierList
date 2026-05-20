package com.ulyup.tierlist.core.ui.components.state

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StatefulContent(
    isLoading: Boolean,
    errorMessage: String?,
    isInitialLoad: Boolean,
    isEmpty: Boolean,
    emptyMessage: String,
    retryLabel: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    when {
        isLoading && isInitialLoad -> LoadingState(modifier = modifier)
        errorMessage != null && isInitialLoad -> ErrorState(
            message = errorMessage,
            modifier = modifier,
            retryLabel = retryLabel,
            onRetry = onRetry,
        )
        isEmpty -> EmptyState(message = emptyMessage, modifier = modifier)
        else -> content(modifier)
    }
}