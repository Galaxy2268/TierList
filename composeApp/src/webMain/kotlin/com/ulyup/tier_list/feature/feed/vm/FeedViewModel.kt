package com.ulyup.tier_list.feature.feed.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.usecase.GetPublicTierListsUseCase
import com.ulyup.tier_list.domain.tier_list.util.removeById
import com.ulyup.tier_list.domain.tier_list.util.setFavourite
import com.ulyup.tier_list.domain.tier_list.util.setTitle
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import kotlinx.coroutines.launch

class FeedViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getPublicTierListsUseCase: GetPublicTierListsUseCase,
) : StatefulViewModel<FeedAction, FeedState>(FeedState()) {

    init {
        viewModelScope.launch {
            observeCurrentUserUseCase(Unit).collect { user ->
                updateState { it.copy(currentUserId = user?.id) }
            }
        }
    }

    override suspend fun handleAction(action: FeedAction) {
        when (action) {
            LoadFeedAction -> load()
            is RemoveTierListAction -> updateState {
                it.copy(tierLists = it.tierLists.removeById(action.tierListId))
            }
            is SetFavouriteAction -> updateState {
                it.copy(tierLists = it.tierLists.setFavourite(action.tierListId, action.isFavourite))
            }
            is SetTitleAction -> updateState {
                it.copy(tierLists = it.tierLists.setTitle(action.tierListId, action.title))
            }
            is ChangeSearchQueryAction -> updateState { it.copy(searchQuery = action.value) }
            is ChangeSortOrderAction -> updateState { it.copy(sortOrder = action.sort) }
            ToggleFavouritesOnlyAction -> updateState { it.copy(favouritesOnly = !it.favouritesOnly) }
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getPublicTierListsUseCase(Unit).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { tierLists ->
                updateState { it.withSuccess().copy(tierLists = tierLists) }
            },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}
