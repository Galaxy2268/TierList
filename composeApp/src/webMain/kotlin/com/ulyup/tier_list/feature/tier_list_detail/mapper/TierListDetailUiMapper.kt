package com.ulyup.tier_list.feature.tier_list_detail.mapper

import com.ulyup.tier_list.domain.tier_list.model.TierListDetail
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailState
import com.ulyup.tier_list.model.Tier

fun TierListDetailState.applyDetail(
    detail: TierListDetail,
    currentUserId: Int?,
): TierListDetailState {
    val sortedItems = detail.items.sortedBy { it.position }
    return copy(
        isLoading = false,
        errorMessage = null,
        title = detail.tierList.title,
        isPublic = detail.tierList.isPublic,
        isOwner = currentUserId != null && currentUserId == detail.tierList.ownerId,
        isUpdatingVisibility = false,
        renameDialog = null,
        isDeleteConfirmVisible = false,
        isDeleting = false,
        deleteErrorMessage = null,
        itemsByTier = Tier.entries.associateWith { tier ->
            sortedItems.filter { it.tier == tier }
        },
        unrankedItems = sortedItems.filter { it.tier == null },
    )
}
