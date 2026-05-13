package com.ulyup.tierlist.data.repository

import com.ulyup.tierlist.data.db.DatabaseFactory.dbQuery
import com.ulyup.tierlist.data.db.tables.TierlistItemsTable
import com.ulyup.tierlist.domain.model.TierlistItem
import com.ulyup.tierlist.domain.repository.ItemRepository
import com.ulyup.tierlist.model.Tier
import org.jetbrains.exposed.v1.core.Op
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
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

    override suspend fun findById(id: Int): TierlistItem? = dbQuery { fetchById(id) }

    override suspend fun findByTierlistId(tierlistId: Int): List<TierlistItem> = dbQuery {
        TierlistItemsTable.selectAll()
            .where { TierlistItemsTable.tierlistId eq tierlistId }
            .orderBy(TierlistItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }
    }

    override suspend fun update(id: Int, tierlistId: Int, imageUrl: String): TierlistItem? = dbQuery {
        val count = TierlistItemsTable.update({
            (TierlistItemsTable.id eq id) and (TierlistItemsTable.tierlistId eq tierlistId)
        }) {
            it[TierlistItemsTable.imageUrl] = imageUrl
        }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun delete(id: Int, tierlistId: Int): Boolean = dbQuery {
        val item = fetchById(id) ?: return@dbQuery false
        if (item.tierlistId != tierlistId) return@dbQuery false
        TierlistItemsTable.deleteWhere { TierlistItemsTable.id eq id }
        reindexTier(tierlistId, item.tier)
        true
    }

    override suspend fun nextPosition(tierlistId: Int, tier: Tier?): Int = dbQuery {
        TierlistItemsTable.selectAll()
            .where { (TierlistItemsTable.tierlistId eq tierlistId) and tierMatches(tier) }
            .count().toInt()
    }

    private fun reindexTier(tierlistId: Int, tier: Tier?) {
        val items = TierlistItemsTable.selectAll()
            .where { (TierlistItemsTable.tierlistId eq tierlistId) and tierMatches(tier) }
            .orderBy(TierlistItemsTable.position to SortOrder.ASC)
            .map { it[TierlistItemsTable.id] to it[TierlistItemsTable.position] }
        items.forEachIndexed { index, (itemId, currentPosition) ->
            if (currentPosition != index) {
                TierlistItemsTable.update({ TierlistItemsTable.id eq itemId }) {
                    it[TierlistItemsTable.position] = index
                }
            }
        }
    }

    private fun tierMatches(tier: Tier?): Op<Boolean> = TierlistItemsTable.tier eq tier

    private fun fetchById(id: Int): TierlistItem? =
        TierlistItemsTable.selectAll().where { TierlistItemsTable.id eq id }.singleOrNull()?.toItem()

    private fun ResultRow.toItem(): TierlistItem = TierlistItem(
        id = this[TierlistItemsTable.id],
        tierlistId = this[TierlistItemsTable.tierlistId],
        imageUrl = this[TierlistItemsTable.imageUrl],
        tier = this[TierlistItemsTable.tier],
        position = this[TierlistItemsTable.position],
    )
}