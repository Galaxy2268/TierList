package com.ulyup.tier_list.domain.tier_list.util

import com.ulyup.tier_list.domain.tier_list.model.TierList

fun List<TierList>.favouritesFirst(): List<TierList> = sortedByDescending { it.isFavourite }
