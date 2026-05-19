package com.ulyup.tierlist.core.ui.components.state

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import com.ulyup.tierlist.core.ui.token.aPadding24

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(aPadding24),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            style = appTypography.bodyLarge,
            color = appColors.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
