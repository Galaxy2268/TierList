package com.ulyup.tier_list.feature.tier_list_detail.util

import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailState

fun TierListDetailState.findItem(itemId: Int): TierListItem? =
    itemsByTier.values.firstNotNullOfOrNull { items -> items.firstOrNull { it.id == itemId } }
        ?: unrankedItems.firstOrNull { it.id == itemId }
