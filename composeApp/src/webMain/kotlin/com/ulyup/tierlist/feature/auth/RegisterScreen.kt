package com.ulyup.tierlist.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(aPadding24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Register (placeholder)",
            style = appTypography.titleLarge,
            color = appColors.onBackground,
        )
        VBox16
        Button(onClick = onBack) {
            Text(text = "Back to Login")
        }
    }
}