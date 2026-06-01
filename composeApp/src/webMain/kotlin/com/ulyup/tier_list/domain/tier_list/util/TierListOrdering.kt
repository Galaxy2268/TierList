package com.ulyup.tier_list.domain.tier_list.util

import com.ulyup.tier_list.domain.tier_list.model.TierList

fun List<TierList>.favouritesFirst(): List<TierList> = sortedByDescending { it.isFavourite }

fun List<TierList>.mapById(id: Int, transform: (TierList) -> TierList): List<TierList> =
    map { if (it.id == id) transform(it) else it }

fun List<TierList>.removeById(id: Int): List<TierList> = filterNot { it.id == id }

fun List<TierList>.setFavourite(id: Int, isFavourite: Boolean): List<TierList> =
    mapById(id) { it.copy(isFavourite = isFavourite) }.favouritesFirst()

fun List<TierList>.setTitle(id: Int, title: String): List<TierList> =
    mapById(id) { it.copy(title = title) }
