package com.ulyup.tier_list.feature.feed.vm

import com.ulyup.tier_list.domain.tier_list.model.TierListSort

sealed interface FeedAction

data object LoadFeedAction : FeedAction
value class RemoveTierListAction(val tierListId: Int) : FeedAction
data class SetFavouriteAction(val tierListId: Int, val isFavourite: Boolean) : FeedAction
data class SetTitleAction(val tierListId: Int, val title: String) : FeedAction

value class ChangeSearchQueryAction(val value: String) : FeedAction
value class ChangeSortOrderAction(val sort: TierListSort) : FeedAction
data object ToggleFavouritesOnlyAction : FeedAction
