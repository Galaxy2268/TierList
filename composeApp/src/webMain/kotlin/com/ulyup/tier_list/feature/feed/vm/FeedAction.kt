package com.ulyup.tier_list.feature.feed.vm

sealed interface FeedAction

data object LoadFeedAction : FeedAction
