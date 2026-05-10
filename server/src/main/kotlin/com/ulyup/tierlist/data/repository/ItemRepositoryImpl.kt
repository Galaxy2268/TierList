package com.ulyup.tierlist.data.repository

import com.ulyup.tierlist.data.db.DatabaseFactory.dbQuery
import com.ulyup.tierlist.data.db.tables.TierlistItemsTable
import com.ulyup.tierlist.domain.model.TierlistItem
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.model.Tier
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

class ItemRepositoryImpl : ItemRepository {

    override suspend fun create(tierlistId: Int, imageUrl: String, tier: Tier?, position: Int): TierlistItem = dbQuery {
        val newId = TierlistItemsTable.insert {
            it[TierlistItemsTable.tierlistId] = tierlistId
            it[TierlistItemsTable.imageUrl] = imageUrl
            it[TierlistItemsTable.tier] = tier
            it[TierlistItemsTable.position] = position
        } get TierlistItemsTable.id
        TierlistItem(newId, tierlistId, imageUrl, tier, position)
    }

    override suspend fun findById(id: Int): TierlistItem? = dbQuery {
        TierlistItemsTable.selectAll()
            .where { TierlistItemsTable.id eq id }
            .singleOrNull()?.toItem()
    }

    override suspend fun findByTierlistId(tierlistId: Int): List<TierlistItem> = dbQuery {
        TierlistItemsTable.selectAll()
            .where { TierlistItemsTable.tierlistId eq tierlistId }
            .orderBy(TierlistItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }
    }

    override suspend fun update(id: Int, imageUrl: String, tier: Tier?, position: Int): TierlistItem? = dbQuery {
        val updated = TierlistItemsTable.update({ TierlistItemsTable.id eq id }) {
            it[TierlistItemsTable.imageUrl] = imageUrl
            it[TierlistItemsTable.tier] = tier
            it[TierlistItemsTable.position] = position
        }
        if (updated == 0) null
        else TierlistItemsTable.selectAll()
            .where { TierlistItemsTable.id eq id }
            .singleOrNull()?.toItem()
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TierlistItemsTable.deleteWhere { TierlistItemsTable.id eq id } > 0
    }

    private fun ResultRow.toItem(): TierlistItem = TierlistItem(
        id = this[TierlistItemsTable.id],
        tierlistId = this[TierlistItemsTable.tierlistId],
        imageUrl = this[TierlistItemsTable.imageUrl],
        tier = this[TierlistItemsTable.tier],
        position = this[TierlistItemsTable.position],
    )
}