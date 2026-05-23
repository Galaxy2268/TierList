package com.ulyup.tier_list.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.components.state.StatefulContent
import com.ulyup.tier_list.core.ui.components.tier_list.TierListCard
import com.ulyup.tier_list.core.ui.token.gap12
import com.ulyup.tier_list.core.ui.token.paddingV16H24
import com.ulyup.tier_list.core.ui.token.size280
import com.ulyup.tier_list.feature.feed.vm.FeedViewModel
import com.ulyup.tier_list.feature.feed.vm.LoadFeedAction
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.error_action_retry
import com.ulyup.tier_list.resources.feed_empty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreen(
    onOpenTierList: (Int) -> Unit,
) {
    val viewModel = koinViewModel<FeedViewModel>()
    val state = viewModel.uiState

    LaunchedEffect(Unit) { viewModel.onAction(LoadFeedAction) }

    AppScaffold { padding ->
        StatefulContent(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isEmpty = state.tierLists.isEmpty(),
            emptyMessage = stringResource(Res.string.feed_empty),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = { viewModel.onAction(LoadFeedAction) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { contentModifier ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = size280),
                modifier = contentModifier,
                contentPadding = paddingV16H24,
                verticalArrangement = Arrangement.spacedBy(gap12),
                horizontalArrangement = Arrangement.spacedBy(gap12),
            ) {
                items(state.tierLists, key = { it.id }) { tierList ->
                    TierListCard(
                        tierList = tierList,
                        onClick = { onOpenTierList(tierList.id) },
                    )
                }
            }
        }
    }
}