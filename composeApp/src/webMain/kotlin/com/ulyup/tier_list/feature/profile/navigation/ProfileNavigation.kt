package com.ulyup.tier_list.feature.profile.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tier_list.feature.profile.ProfileScreen
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
