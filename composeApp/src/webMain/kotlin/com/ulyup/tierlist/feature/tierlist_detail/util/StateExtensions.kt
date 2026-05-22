package com.ulyup.tierlist.feature.tierlist_detail.util

import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.feature.tierlist_detail.vm.TierlistDetailState

fun TierlistDetailState.findItem(itemId: Int): Item? =
    itemsByTier.values.firstNotNullOfOrNull { items -> items.firstOrNull { it.id == itemId } }
        ?: unrankedItems.firstOrNull { it.id == itemId }
