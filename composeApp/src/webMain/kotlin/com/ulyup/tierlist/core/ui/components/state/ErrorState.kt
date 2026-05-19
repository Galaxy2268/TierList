package com.ulyup.tierlist.core.ui.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.aPadding24

@Composable
fun ErrorState(
    message: String,
    modifier: Modifier = Modifier,
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
        if (onRetry != null) {
            VBox16
            Button(onClick = onRetry) {
                Text(
                    text = "Retry",
                    style = appTypography.labelLarge,
                )
            }
        }
    }
}
