package com.ulyup.tierlist.feature.mylists.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ulyup.tierlist.feature.mylists.MyListsScreen
import kotlinx.serialization.Serializable

@Serializable
data object MyListsGraph

@Serializable
data object MyListsRoute

fun NavGraphBuilder.myListsGraph(onOpenTierlist: (Int) -> Unit) {
    navigation<MyListsGraph>(startDestination = MyListsRoute) {
        composable<MyListsRoute> { MyListsScreen(onOpenTierlist = onOpenTierlist) }
    }
}
