package com.ulyup.tier_list.data.repository

import com.ulyup.tier_list.data.db.DatabaseFactory.dbQuery
import com.ulyup.tier_list.data.db.tables.FavouritesTable
import com.ulyup.tier_list.domain.repository.FavouriteRepository
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertIgnore
import org.jetbrains.exposed.v1.jdbc.selectAll

class FavouriteRepositoryImpl : FavouriteRepository {

    override suspend fun add(userId: Int, tierListId: Int): Unit = dbQuery {
        FavouritesTable.insertIgnore {
            it[FavouritesTable.userId] = userId
            it[FavouritesTable.tierListId] = tierListId
        }
    }

    override suspend fun remove(userId: Int, tierListId: Int): Unit = dbQuery {
        FavouritesTable.deleteWhere { ownedBy(userId, tierListId) }
    }

    override suspend fun isFavourite(userId: Int, tierListId: Int): Boolean = dbQuery {
        FavouritesTable.selectAll().where { ownedBy(userId, tierListId) }.count() > 0
    }

    override suspend fun findTierListIdsByUser(userId: Int): Set<Int> = dbQuery {
        FavouritesTable.selectAll()
            .where { FavouritesTable.userId eq userId }
            .mapTo(mutableSetOf()) { it[FavouritesTable.tierListId] }
    }

    private fun ownedBy(userId: Int, tierListId: Int) =
        (FavouritesTable.userId eq userId) and (FavouritesTable.tierListId eq tierListId)
}
