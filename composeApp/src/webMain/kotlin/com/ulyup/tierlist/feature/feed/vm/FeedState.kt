package com.ulyup.tierlist.feature.feed.vm

import com.ulyup.tierlist.core.mvi.LoadableState
import com.ulyup.tierlist.domain.tierlist.model.Tierlist

data class FeedState(
    override val isLoading: Boolean = false,
    val tierlists: List<Tierlist> = emptyList(),
    override val errorMessage: String? = null,
) : LoadableState<FeedState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}