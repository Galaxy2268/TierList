package com.ulyup.tier_list.domain.repository

interface FavouriteRepository {
    suspend fun add(userId: Int, tierListId: Int)
    suspend fun remove(userId: Int, tierListId: Int)
    suspend fun isFavourite(userId: Int, tierListId: Int): Boolean
    suspend fun findTierListIdsByUser(userId: Int): Set<Int>
}
