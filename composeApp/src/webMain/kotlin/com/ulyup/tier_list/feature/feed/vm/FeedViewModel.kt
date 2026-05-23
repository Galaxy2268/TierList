package com.ulyup.tier_list.feature.feed.vm

import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.usecase.GetPublicTierListsUseCase

class FeedViewModel(
    private val getPublicTierListsUseCase: GetPublicTierListsUseCase,
) : StatefulViewModel<FeedAction, FeedState>(FeedState()) {

    override suspend fun handleAction(action: FeedAction) {
        when (action) {
            LoadFeedAction -> load()
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getPublicTierListsUseCase(Unit).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { tierLists ->
                updateState { it.withLoaded().copy(tierLists = tierLists) }
            },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}