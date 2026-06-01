package com.ulyup.tier_list.feature.feed.vm

import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListSort
import com.ulyup.tier_list.domain.tier_list.util.filteredAndSorted

data class FeedState(
    override val isLoading: Boolean = false,
    val tierLists: List<TierList> = emptyList(),
    val currentUserId: Int? = null,
    val searchQuery: String = "",
    val sortOrder: TierListSort = TierListSort.NEWEST,
    val favouritesOnly: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<FeedState> {
    val visibleTierLists: List<TierList>
        get() = tierLists.filteredAndSorted(searchQuery, sortOrder, favouritesOnly)

    fun isOwner(tierList: TierList): Boolean =
        currentUserId != null && currentUserId == tierList.ownerId

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}
