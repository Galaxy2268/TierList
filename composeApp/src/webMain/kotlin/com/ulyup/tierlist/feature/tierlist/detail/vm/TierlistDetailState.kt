package com.ulyup.tierlist.feature.tierlist.detail.vm

import com.ulyup.tierlist.core.mvi.LoadableState
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.model.Tier

data class TierlistDetailState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val title: String = "",
    val itemsByTier: Map<Tier, List<Item>> = emptyMap(),
    val unrankedItems: List<Item> = emptyList(),
) : LoadableState<TierlistDetailState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}