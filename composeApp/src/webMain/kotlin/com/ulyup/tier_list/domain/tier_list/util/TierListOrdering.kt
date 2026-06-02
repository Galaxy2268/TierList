package com.ulyup.tier_list.domain.tier_list.util

import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListSort

fun List<TierList>.favouritesFirst(): List<TierList> = sortedByDescending { it.isFavourite }

fun List<TierList>.filteredAndSorted(
    query: String,
    sortOrder: TierListSort,
    favouritesOnly: Boolean,
): List<TierList> {
    val trimmedQuery = query.trim()
    val filtered = filter { tierList ->
        (!favouritesOnly || tierList.isFavourite) &&
            (trimmedQuery.isBlank() || tierList.title.contains(trimmedQuery, ignoreCase = true))
    }
    val sorted = when (sortOrder) {
        TierListSort.NEWEST -> filtered.sortedByDescending { it.createdAt }
        TierListSort.OLDEST -> filtered.sortedBy { it.createdAt }
        TierListSort.TITLE_ASC -> filtered.sortedBy { it.title.lowercase() }
        TierListSort.TITLE_DESC -> filtered.sortedByDescending { it.title.lowercase() }
    }
    return sorted.favouritesFirst()
}

fun List<TierList>.mapById(id: Int, transform: (TierList) -> TierList): List<TierList> =
    map { if (it.id == id) transform(it) else it }

fun List<TierList>.removeById(id: Int): List<TierList> = filterNot { it.id == id }

fun List<TierList>.setFavourite(id: Int, isFavourite: Boolean): List<TierList> =
    mapById(id) { it.copy(isFavourite = isFavourite) }.favouritesFirst()

fun List<TierList>.setTitle(id: Int, title: String): List<TierList> =
    mapById(id) { it.copy(title = title) }
