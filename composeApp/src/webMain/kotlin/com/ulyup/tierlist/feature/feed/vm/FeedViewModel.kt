package com.ulyup.tierlist.feature.feed.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.tierlist.usecase.GetPublicTierlistsUseCase
import kotlinx.coroutines.launch

class FeedViewModel(
    private val getPublicTierlistsUseCase: GetPublicTierlistsUseCase,
) : StatefulViewModel<FeedAction, FeedState>(FeedState()) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: FeedAction) {
        when (action) {
            LoadFeedAction -> load()
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getPublicTierlistsUseCase(Unit).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { tierlists ->
                updateState { it.withLoaded().copy(tierlists = tierlists) }
            },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}