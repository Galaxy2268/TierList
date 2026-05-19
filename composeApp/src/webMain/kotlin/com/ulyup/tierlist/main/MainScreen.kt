package com.ulyup.tierlist.main

import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ulyup.tierlist.core.navigation.AppNavHost
import com.ulyup.tierlist.feature.feed.navigation.FeedGraph
import com.ulyup.tierlist.feature.mylists.navigation.MyListsGraph
import com.ulyup.tierlist.feature.profile.navigation.ProfileGraph
import com.ulyup.tierlist.main.vm.MainViewModel
import com.ulyup.tierlist.main.vm.RetryBootstrapAction
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.top_nav_feed
import com.ulyup.tierlist.resources.top_nav_mylists
import com.ulyup.tierlist.resources.top_nav_profile
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.reflect.KClass

@Composable
fun MainScreen() {
    val viewModel = koinViewModel<MainViewModel>()
    val state = viewModel.uiState

    AppNavHost(
        startDestination = state.startDestination,
        onRetryBootstrap = { viewModel.onAction(RetryBootstrapAction) },
        topBar = { currentDestination, onTabSelected ->
            if (currentDestination.isInTabbedArea()) {
                AppTopNavBar(
                    currentDestination = currentDestination,
                    onTabSelected = { tab -> onTabSelected(tab.target) },
                )
            }
        },
    )
}

private enum class AppTab(
    val graphClass: KClass<*>,
    val target: Any,
    val titleRes: StringResource,
) {
    Feed(FeedGraph::class, FeedGraph, Res.string.top_nav_feed),
    MyLists(MyListsGraph::class, MyListsGraph, Res.string.top_nav_mylists),
    Profile(ProfileGraph::class, ProfileGraph, Res.string.top_nav_profile),
}

private fun NavDestination?.isInTabbedArea(): Boolean =
    AppTab.entries.any { tab -> this?.matches(tab) == true }

private fun NavDestination.matches(tab: AppTab): Boolean =
    hierarchy.any { it.hasRoute(tab.graphClass) }

@Composable
private fun AppTopNavBar(
    currentDestination: NavDestination?,
    onTabSelected: (AppTab) -> Unit,
) {
    val selectedIndex = AppTab.entries
        .indexOfFirst { tab -> currentDestination?.matches(tab) == true }
        .coerceAtLeast(0)

    PrimaryTabRow(
        selectedTabIndex = selectedIndex,
        containerColor = appColors.surface,
        contentColor = appColors.onSurface,
    ) {
        AppTab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onTabSelected(tab) },
                selectedContentColor = appColors.primary,
                unselectedContentColor = appColors.onSurfaceVariant,
                text = {
                    Text(
                        text = stringResource(tab.titleRes),
                        style = appTypography.labelLarge,
                    )
                },
            )
        }
    }
}
