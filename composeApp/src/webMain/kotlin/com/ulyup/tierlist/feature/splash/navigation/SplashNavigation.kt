package com.ulyup.tierlist.feature.splash.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tierlist.feature.splash.SplashScreen
import kotlinx.serialization.Serializable

@Serializable
data object SplashGraph

@Serializable
data object SplashRoute

fun NavGraphBuilder.splashGraph() {
    navigation<SplashGraph>(startDestination = SplashRoute) {
        composable<SplashRoute> { SplashScreen() }
    }
}