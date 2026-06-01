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
import com.ulyup.tier_list.feature.feed.vm.RemoveTierListAction
import com.ulyup.tier_list.feature.feed.vm.SetFavouriteAction
import com.ulyup.tier_list.feature.feed.vm.SetTitleAction
import com.ulyup.tier_list.feature.shared.tier_list.TierListOptionsHost
import com.ulyup.tier_list.feature.shared.tier_list.rememberTierListOptionDispatch
import com.ulyup.tier_list.feature.shared.tier_list.vm.FavouriteChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionsViewModel
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListDeletedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TitleChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.VisibilityChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.toOptionTarget
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
    val optionsViewModel = koinViewModel<TierListOptionsViewModel>()
    val state = viewModel.uiState
    val dispatchOption = rememberTierListOptionDispatch(optionsViewModel)

    LaunchedEffect(Unit) { viewModel.onAction(LoadFeedAction) }

    TierListOptionsHost(optionsViewModel) { event ->
        when (event) {
            is TierListDeletedEvent -> viewModel.onAction(RemoveTierListAction(event.tierListId))
            is VisibilityChangedEvent ->
                if (!event.isPublic) viewModel.onAction(RemoveTierListAction(event.tierListId))
            is FavouriteChangedEvent ->
                viewModel.onAction(SetFavouriteAction(event.tierListId, event.isFavourite))
            is TitleChangedEvent -> viewModel.onAction(SetTitleAction(event.tierListId, event.title))
            else -> Unit
        }
    }

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
                        isOwner = state.isOwner(tierList),
                        onClick = { onOpenTierList(tierList.id) },
                        onOption = { option -> dispatchOption(tierList.toOptionTarget(state.isOwner(tierList)), option) },
                    )
                }
            }
        }
    }
}
