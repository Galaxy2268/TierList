package com.ulyup.tierlist.feature.feed.vm

sealed interface FeedAction

data object LoadFeedAction : FeedAction
