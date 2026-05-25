package com.ulyup.tier_list.feature.tier_list_detail.vm

sealed interface TierListDetailEvent

data object TierListDeletedEvent : TierListDetailEvent
