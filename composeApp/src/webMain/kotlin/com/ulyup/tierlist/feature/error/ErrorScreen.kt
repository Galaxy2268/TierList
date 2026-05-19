package com.ulyup.tierlist.feature.error

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.components.state.ErrorState
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.error_action_retry
import com.ulyup.tierlist.resources.error_message
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorScreen(onRetry: () -> Unit) {
    AppScaffold { padding ->
        ErrorState(
            message = stringResource(Res.string.error_message),
            modifier = Modifier.padding(padding),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = onRetry,
        )
    }
}