package com.ulyup.tier_list.feature.shared.tier_list.vm

import com.ulyup.tier_list.domain.tier_list.model.TierList

data class TierListOptionTarget(
    val id: Int,
    val title: String,
    val isPublic: Boolean,
    val isFavourite: Boolean,
    val isOwner: Boolean,
)

fun TierList.toOptionTarget(isOwner: Boolean): TierListOptionTarget = TierListOptionTarget(
    id = id,
    title = title,
    isPublic = isPublic,
    isFavourite = isFavourite,
    isOwner = isOwner,
)
