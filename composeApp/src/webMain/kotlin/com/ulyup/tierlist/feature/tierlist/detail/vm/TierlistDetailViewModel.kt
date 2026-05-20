package com.ulyup.tierlist.feature.tierlist.detail.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.tierlist.usecase.GetTierlistDetailUseCase
import com.ulyup.tierlist.feature.tierlist.detail.mapper.toUiState
import kotlinx.coroutines.launch

class TierlistDetailViewModel(
    private val tierlistId: Int,
    private val getTierlistDetailUseCase: GetTierlistDetailUseCase,
) : StatefulViewModel<TierlistDetailAction, TierlistDetailState>(TierlistDetailState()) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: TierlistDetailAction) {
        when (action) {
            LoadDetailAction -> load()
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getTierlistDetailUseCase(tierlistId).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { detail -> updateState { detail.toUiState() } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}