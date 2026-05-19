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
            onLoading = { updateState { it.copy(isLoading = true, errorMessage = null) } },
            onSuccess = { tierlists ->
                updateState { it.copy(isLoading = false, tierlists = tierlists) }
            },
            onError = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message,
                    )
                }
            },
        )
    }
}
