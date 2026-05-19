package com.ulyup.tierlist.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.components.state.EmptyState
import com.ulyup.tierlist.core.ui.components.state.ErrorState
import com.ulyup.tierlist.core.ui.components.state.LoadingState
import com.ulyup.tierlist.core.ui.components.tierlist.TierlistCard
import com.ulyup.tierlist.core.ui.token.paddingV16H24
import com.ulyup.tierlist.core.ui.token.tierlistCardMinWidth
import com.ulyup.tierlist.feature.feed.vm.FeedViewModel
import com.ulyup.tierlist.feature.feed.vm.LoadFeedAction
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.error_action_retry
import com.ulyup.tierlist.resources.feed_empty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FeedScreen() {
    val viewModel = koinViewModel<FeedViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        val modifier = Modifier.fillMaxSize().padding(padding)
        when {
            state.isLoading && state.tierlists.isEmpty() -> LoadingState(modifier = modifier)
            state.errorMessage != null && state.tierlists.isEmpty() -> ErrorState(
                message = state.errorMessage,
                modifier = modifier,
                retryLabel = stringResource(Res.string.error_action_retry),
                onRetry = { viewModel.onAction(LoadFeedAction) },
            )
            state.tierlists.isEmpty() -> EmptyState(
                message = stringResource(Res.string.feed_empty),
                modifier = modifier,
            )
            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = tierlistCardMinWidth),
                modifier = modifier,
                contentPadding = paddingV16H24,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.tierlists, key = { it.id }) { tierlist ->
                    TierlistCard(tierlist = tierlist)
                }
            }
        }
    }
}
