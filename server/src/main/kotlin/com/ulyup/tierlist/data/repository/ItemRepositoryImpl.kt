package com.ulyup.tierlist.data.repository

import com.ulyup.tierlist.data.db.DatabaseFactory.dbQuery
import com.ulyup.tierlist.data.db.tables.TierlistItemsTable
import com.ulyup.tierlist.data.db.tables.TierlistsTable
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

    override suspend fun create(tierlistId: Int, userId: Int, imageUrl: String): TierlistItem? = dbQuery {
        if (!ownsParent(tierlistId, userId)) return@dbQuery null
        val position = loadTier(tierlistId, tier = null).size
        val newId = TierlistItemsTable.insert {
            it[TierlistItemsTable.tierlistId] = tierlistId
            it[TierlistItemsTable.imageUrl] = imageUrl
            it[TierlistItemsTable.tier] = null
            it[TierlistItemsTable.position] = position
        } get TierlistItemsTable.id
        TierlistItem(newId, tierlistId, imageUrl, tier = null, position = position)
    }

    override suspend fun findById(id: Int): TierlistItem? = dbQuery { fetchById(id) }

    override suspend fun findByTierlistId(tierlistId: Int): List<TierlistItem> = dbQuery {
        TierlistItemsTable.selectAll()
            .where { TierlistItemsTable.tierlistId eq tierlistId }
            .orderBy(TierlistItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }
    }

    override suspend fun update(id: Int, tierlistId: Int, userId: Int, imageUrl: String): TierlistItem? = dbQuery {
        if (!ownsParent(tierlistId, userId)) return@dbQuery null
        val count = TierlistItemsTable.update({
            (TierlistItemsTable.id eq id) and (TierlistItemsTable.tierlistId eq tierlistId)
        }) {
            it[TierlistItemsTable.imageUrl] = imageUrl
        }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun delete(id: Int, tierlistId: Int, userId: Int): TierlistItem? = dbQuery {
        if (!ownsParent(tierlistId, userId)) return@dbQuery null
        val item = fetchById(id) ?: return@dbQuery null
        if (item.tierlistId != tierlistId) return@dbQuery null
        TierlistItemsTable.deleteWhere { TierlistItemsTable.id eq id }
        compactTier(tierlistId, item.tier)
        item
    }

    override suspend fun move(id: Int, tierlistId: Int, userId: Int, newTier: Tier?, newPosition: Int): TierlistItem? = dbQuery {
        if (!ownsParent(tierlistId, userId)) return@dbQuery null
        val item = fetchById(id) ?: return@dbQuery null
        if (item.tierlistId != tierlistId) return@dbQuery null

        val oldTier = item.tier
        // Build the new ordering of the destination tier: take everyone currently there,
        // pull out the moved item if it was already in this tier, then insert at newPosition.
        val destination = loadTier(tierlistId, newTier)
            .filter { it.id != id }
            .toMutableList()
            .also { it.add(newPosition.coerceIn(0, it.size), item) }
        assignPositions(newTier, destination)
        if (oldTier != newTier) compactTier(tierlistId, oldTier)

        fetchById(id)
    }

    private fun ownsParent(tierlistId: Int, userId: Int): Boolean =
        TierlistsTable.selectAll()
            .where { (TierlistsTable.id eq tierlistId) and (TierlistsTable.userId eq userId) }
            .count() > 0

    private fun loadTier(tierlistId: Int, tier: Tier?): List<TierlistItem> =
        TierlistItemsTable.selectAll()
            .where { (TierlistItemsTable.tierlistId eq tierlistId) and tierMatches(tier) }
            .orderBy(TierlistItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }

    private fun assignPositions(tier: Tier?, items: List<TierlistItem>) {
        items.forEachIndexed { index, currentItem ->
            TierlistItemsTable.update({ TierlistItemsTable.id eq currentItem.id }) {
                it[TierlistItemsTable.tier] = tier
                it[TierlistItemsTable.position] = index
            }
        }
    }

    private fun compactTier(tierlistId: Int, tier: Tier?) {
        loadTier(tierlistId, tier).forEachIndexed { index, currentItem ->
            if (currentItem.position != index) {
                TierlistItemsTable.update({ TierlistItemsTable.id eq currentItem.id }) {
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
