package com.ulyup.tierlist.core.navigation

import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavController
import com.ulyup.tierlist.feature.auth.navigation.navigateToAuth
import com.ulyup.tierlist.feature.auth.navigation.navigateToRegister
import com.ulyup.tierlist.feature.feed.navigation.navigateToFeed
import com.ulyup.tierlist.feature.mylists.navigation.navigateToMyLists

class AppNavigator(private val navController: NavController) {

    fun toAuth() {
        navController.navigateToAuth()
    }

    fun toFeed() {
        navController.navigateToFeed()
    }

    fun toMyLists() {
        navController.navigateToMyLists()
    }

    fun toRegister() {
        navController.navigateToRegister()
    }

    fun back() {
        navController.popBackStack()
    }
}

val LocalAppNavigator = compositionLocalOf<AppNavigator> {
    error("AppNavigator not provided")
}