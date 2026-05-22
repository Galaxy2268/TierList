package com.ulyup.tierlist.feature.tierlist_detail.mapper

import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail
import com.ulyup.tierlist.feature.tierlist_detail.vm.TierlistDetailState
import com.ulyup.tierlist.model.Tier

fun TierlistDetailState.applyDetail(
    detail: TierlistDetail,
    currentUserId: Int?,
): TierlistDetailState {
    val sortedItems = detail.items.sortedBy { it.position }
    return copy(
        isLoading = false,
        errorMessage = null,
        title = detail.tierlist.title,
        isOwner = currentUserId != null && currentUserId == detail.tierlist.ownerId,
        itemsByTier = Tier.entries.associateWith { tier ->
            sortedItems.filter { it.tier == tier }
        },
        unrankedItems = sortedItems.filter { it.tier == null },
    )
}
