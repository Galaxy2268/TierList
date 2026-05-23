package com.ulyup.tier_list.domain.tier_list.model

data class TierListDetail(
    val tierList: TierList,
    val items: List<TierListItem>,
)
