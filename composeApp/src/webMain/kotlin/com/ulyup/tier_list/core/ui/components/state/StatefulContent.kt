package com.ulyup.tier_list.core.ui.components.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun StatefulContent(
    isLoading: Boolean,
    errorMessage: String?,
    isEmpty: Boolean,
    emptyMessage: String,
    retryLabel: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
) {
    var isInitialLoad by remember { mutableStateOf(true) }
    LaunchedEffect(isLoading, errorMessage) {
        if (!isLoading && errorMessage == null) isInitialLoad = false
    }

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