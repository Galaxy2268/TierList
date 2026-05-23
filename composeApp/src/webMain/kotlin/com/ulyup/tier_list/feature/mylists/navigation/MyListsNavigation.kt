package com.ulyup.tier_list.feature.mylists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tier_list.feature.mylists.MyListsScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyListsGraph

@Serializable
data object MyListsRoute

fun NavGraphBuilder.myListsGraph(onOpenTierList: (Int) -> Unit) {
    navigation<MyListsGraph>(startDestination = MyListsRoute) {
        composable<MyListsRoute> { MyListsScreen(onOpenTierList = onOpenTierList) }
    }
}
