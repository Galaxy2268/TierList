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
import com.ulyup.tierlist.core.ui.components.state.StatefulContent
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
fun FeedScreen(
    onOpenTierlist: (Int) -> Unit,
) {
    val viewModel = koinViewModel<FeedViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        StatefulContent(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isInitialLoad = state.tierlists.isEmpty(),
            isEmpty = state.tierlists.isEmpty(),
            emptyMessage = stringResource(Res.string.feed_empty),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = { viewModel.onAction(LoadFeedAction) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { contentModifier ->
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = tierlistCardMinWidth),
                modifier = contentModifier,
                contentPadding = paddingV16H24,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.tierlists, key = { it.id }) { tierlist ->
                    TierlistCard(
                        tierlist = tierlist,
                        onClick = { onOpenTierlist(tierlist.id) },
                    )
                }
            }
        }
    }
}