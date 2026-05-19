package com.ulyup.tierlist.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize().background(appColors.background),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Tier List",
            style = appTypography.displayLarge,
            color = appColors.onBackground,
        )
    }
}