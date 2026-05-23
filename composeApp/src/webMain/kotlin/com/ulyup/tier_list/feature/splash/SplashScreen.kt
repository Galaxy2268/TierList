package com.ulyup.tier_list.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.theme.appColors

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().background(appColors.background))
}