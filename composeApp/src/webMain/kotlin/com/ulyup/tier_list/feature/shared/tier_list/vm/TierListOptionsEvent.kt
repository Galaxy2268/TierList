package com.ulyup.tier_list.feature.shared.tier_list.vm

import com.ulyup.tier_list.domain.tier_list.model.TierList

sealed interface TierListOptionsEvent

data class TierListDeletedEvent(val tierListId: Int) : TierListOptionsEvent
data class TierListCopiedEvent(val tierList: TierList) : TierListOptionsEvent
data class ItemsClearedEvent(val tierListId: Int) : TierListOptionsEvent
data class FavouriteChangedEvent(val tierListId: Int, val isFavourite: Boolean) : TierListOptionsEvent
data class VisibilityChangedEvent(val tierListId: Int, val isPublic: Boolean) : TierListOptionsEvent
data class TitleChangedEvent(val tierListId: Int, val title: String) : TierListOptionsEvent
data object ShareLinkCopiedEvent : TierListOptionsEvent
data class ShowErrorMessageEvent(val text: String?) : TierListOptionsEvent
