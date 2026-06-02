package com.ulyup.tier_list.feature.mylists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.mvi.ObserveAsEvents
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.components.state.EmptyState
import com.ulyup.tier_list.core.ui.components.state.StatefulContent
import com.ulyup.tier_list.core.ui.components.tier_list.TierListCard
import com.ulyup.tier_list.core.ui.components.tier_list.TierListFilterBar
import com.ulyup.tier_list.core.ui.snackbar.LocalTierListSnackbarHandler
import com.ulyup.tier_list.core.ui.token.gap12
import com.ulyup.tier_list.core.ui.token.paddingT16H24
import com.ulyup.tier_list.core.ui.token.paddingV16H24
import com.ulyup.tier_list.core.ui.token.size280
import com.ulyup.tier_list.feature.mylists.components.CreateTierListDialog
import com.ulyup.tier_list.feature.mylists.components.PremiumUpsellCard
import com.ulyup.tier_list.feature.mylists.vm.ChangeCreateTitleAction
import com.ulyup.tier_list.feature.mylists.vm.ChangeSearchQueryAction
import com.ulyup.tier_list.feature.mylists.vm.ChangeSortOrderAction
import com.ulyup.tier_list.feature.mylists.vm.AddTierListAction
import com.ulyup.tier_list.feature.mylists.vm.ConfirmCreateAction
import com.ulyup.tier_list.feature.mylists.vm.DismissCreateDialogAction
import com.ulyup.tier_list.feature.mylists.vm.LoadMyListsAction
import com.ulyup.tier_list.feature.mylists.vm.MyListsViewModel
import com.ulyup.tier_list.feature.mylists.vm.RemoveTierListAction
import com.ulyup.tier_list.feature.mylists.vm.SetFavouriteAction
import com.ulyup.tier_list.feature.mylists.vm.SetTitleAction
import com.ulyup.tier_list.feature.mylists.vm.SetVisibilityAction
import com.ulyup.tier_list.feature.mylists.vm.ShowCreateDialogAction
import com.ulyup.tier_list.feature.mylists.vm.ShowErrorMessageEvent
import com.ulyup.tier_list.feature.mylists.vm.ToggleCreatePublicAction
import com.ulyup.tier_list.feature.mylists.vm.ToggleFavouritesOnlyAction
import com.ulyup.tier_list.feature.mylists.vm.UpgradePremiumAction
import com.ulyup.tier_list.feature.shared.tier_list.TierListOptionsHost
import com.ulyup.tier_list.feature.shared.tier_list.rememberTierListOptionDispatch
import com.ulyup.tier_list.feature.shared.tier_list.vm.FavouriteChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListCopiedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionsViewModel
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListDeletedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TitleChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.VisibilityChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.toOptionTarget
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.error_action_retry
import com.ulyup.tier_list.resources.filter_no_matches
import com.ulyup.tier_list.resources.ic_add
import com.ulyup.tier_list.resources.mylists_action_create_label
import com.ulyup.tier_list.resources.mylists_empty
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyListsScreen(
    onOpenTierList: (Int) -> Unit,
) {
    val viewModel = koinViewModel<MyListsViewModel>()
    val optionsViewModel = koinViewModel<TierListOptionsViewModel>()
    val state = viewModel.uiState
    val snackbarHandler = LocalTierListSnackbarHandler.current
    val scope = rememberCoroutineScope()
    val dispatchOption = rememberTierListOptionDispatch(optionsViewModel)

    LaunchedEffect(Unit) { viewModel.onAction(LoadMyListsAction) }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is ShowErrorMessageEvent -> scope.launch { snackbarHandler.showError(event.text) }
        }
    }

    TierListOptionsHost(optionsViewModel) { event ->
        when (event) {
            is TierListCopiedEvent -> viewModel.onAction(AddTierListAction(event.tierList))
            is TierListDeletedEvent -> viewModel.onAction(RemoveTierListAction(event.tierListId))
            is FavouriteChangedEvent ->
                viewModel.onAction(SetFavouriteAction(event.tierListId, event.isFavourite))
            is TitleChangedEvent -> viewModel.onAction(SetTitleAction(event.tierListId, event.title))
            is VisibilityChangedEvent ->
                viewModel.onAction(SetVisibilityAction(event.tierListId, event.isPublic))
            else -> Unit
        }
    }

    AppScaffold(
        floatingActionButton = {
            if (state.showCreateFab) {
                FloatingActionButton(onClick = { viewModel.onAction(ShowCreateDialogAction) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = stringResource(Res.string.mylists_action_create_label),
                    )
                }
            }
        },
    ) { padding ->
        StatefulContent(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isEmpty = state.tierLists.isEmpty() && !state.isAtCap,
            emptyMessage = stringResource(Res.string.mylists_empty),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = { viewModel.onAction(LoadMyListsAction) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { contentModifier ->
            val visibleTierLists = state.visibleTierLists
            Column(modifier = contentModifier) {
                TierListFilterBar(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = { viewModel.onAction(ChangeSearchQueryAction(it)) },
                    sortOrder = state.sortOrder,
                    onSortOrderChange = { viewModel.onAction(ChangeSortOrderAction(it)) },
                    favouritesOnly = state.favouritesOnly,
                    onToggleFavouritesOnly = { viewModel.onAction(ToggleFavouritesOnlyAction) },
                    modifier = Modifier.padding(paddingT16H24),
                )
                if (visibleTierLists.isEmpty()) {
                    EmptyState(
                        message = stringResource(Res.string.filter_no_matches),
                        modifier = Modifier.weight(1f),
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = size280),
                        modifier = Modifier.weight(1f),
                        contentPadding = paddingV16H24,
                        verticalArrangement = Arrangement.spacedBy(gap12),
                        horizontalArrangement = Arrangement.spacedBy(gap12),
                    ) {
                        items(visibleTierLists, key = { it.id }) { tierList ->
                            TierListCard(
                                tierList = tierList,
                                isOwner = true,
                                onClick = { onOpenTierList(tierList.id) },
                                onOption = { option -> dispatchOption(tierList.toOptionTarget(isOwner = true), option) },
                            )
                        }
                        if (state.isAtCap) {
                            item(key = "premium-upsell", span = { GridItemSpan(maxLineSpan) }) {
                                PremiumUpsellCard(
                                    isUpgrading = state.isUpgrading,
                                    onUpgrade = { viewModel.onAction(UpgradePremiumAction) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    state.createDialog?.let { dialog ->
        CreateTierListDialog(
            state = dialog,
            onTitleChange = { viewModel.onAction(ChangeCreateTitleAction(it)) },
            onPublicChange = { viewModel.onAction(ToggleCreatePublicAction(it)) },
            onConfirm = { viewModel.onAction(ConfirmCreateAction) },
            onDismiss = { viewModel.onAction(DismissCreateDialogAction) },
        )
    }
}
