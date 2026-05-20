package com.ulyup.tierlist.feature.tierlist.detail.mapper

import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail
import com.ulyup.tierlist.feature.tierlist.detail.vm.TierlistDetailState
import com.ulyup.tierlist.model.Tier

fun TierlistDetail.toUiState(): TierlistDetailState {
    val sortedItems = items.sortedBy { it.position }
    return TierlistDetailState(
        isLoading = false,
        errorMessage = null,
        title = tierlist.title,
        itemsByTier = Tier.entries.associateWith { tier ->
            sortedItems.filter { it.tier == tier }
        },
        unrankedItems = sortedItems.filter { it.tier == null },
    )
}
