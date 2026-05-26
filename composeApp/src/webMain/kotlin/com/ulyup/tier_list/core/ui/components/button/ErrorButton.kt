package com.ulyup.tier_list.core.ui.components.button

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulyup.tier_list.core.ui.token.roundedShape8
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography

@Composable
fun ErrorButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        shape = roundedShape8,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.error,
            contentColor = appColors.onError,
        ),
        modifier = modifier,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = appColors.onError,
                strokeWidth = 2.dp,
                modifier = Modifier.padding(2.dp),
            )
        } else {
            Text(
                text = text,
                style = appTypography.labelLarge,
            )
        }
    }
}
