package com.ulyup.tierlist.core.ui.components.text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography

@Composable
fun ErrorText(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = appTypography.bodySmall,
        color = appColors.error,
        textAlign = TextAlign.Center,
        modifier = modifier.fillMaxWidth(),
    )
}