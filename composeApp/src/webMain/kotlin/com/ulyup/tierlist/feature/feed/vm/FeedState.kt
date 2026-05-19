package com.ulyup.tierlist.feature.feed.vm

import com.ulyup.tierlist.domain.tierlist.model.Tierlist

data class FeedState(
    val isLoading: Boolean = false,
    val tierlists: List<Tierlist> = emptyList(),
    val errorMessage: String? = null,
)
