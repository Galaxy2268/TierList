package com.ulyup.tier_list.feature.feed.vm

import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.domain.tier_list.model.TierList

data class FeedState(
    override val isLoading: Boolean = false,
    val tierLists: List<TierList> = emptyList(),
    override val errorMessage: String? = null,
) : LoadableState<FeedState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}