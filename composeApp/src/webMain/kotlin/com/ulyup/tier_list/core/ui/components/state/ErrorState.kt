package com.ulyup.tier_list.core.ui.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.token.VBox16
import com.ulyup.tier_list.core.ui.token.aPadding24
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography

@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
    retryLabel: String? = null,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(aPadding24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = message,
            style = appTypography.bodyLarge,
            color = appColors.error,
            textAlign = TextAlign.Center,
        )
        if (retryLabel != null && onRetry != null) {
            VBox16
            PrimaryButton(text = retryLabel, onClick = onRetry)
        }
    }
}