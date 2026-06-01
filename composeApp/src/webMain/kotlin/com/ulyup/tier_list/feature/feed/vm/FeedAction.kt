package com.ulyup.tier_list.feature.feed.vm

sealed interface FeedAction

data object LoadFeedAction : FeedAction
value class RemoveTierListAction(val tierListId: Int) : FeedAction
data class SetFavouriteAction(val tierListId: Int, val isFavourite: Boolean) : FeedAction
data class SetTitleAction(val tierListId: Int, val title: String) : FeedAction
