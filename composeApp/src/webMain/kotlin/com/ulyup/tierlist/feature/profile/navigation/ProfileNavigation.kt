package com.ulyup.tierlist.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tierlist.feature.profile.ProfileScreen
import kotlinx.serialization.Serializable

@Serializable
data object ProfileGraph

@Serializable
data object ProfileRoute

fun NavGraphBuilder.profileGraph() {
    navigation<ProfileGraph>(startDestination = ProfileRoute) {
        composable<ProfileRoute> { ProfileScreen() }
    }
}
