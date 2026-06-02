package com.ulyup.tier_list.data.repository

import com.ulyup.tier_list.data.db.DatabaseFactory.dbQuery
import com.ulyup.tier_list.data.db.tables.TierListItemsTable
import com.ulyup.tier_list.data.db.tables.TierListsTable
import com.ulyup.tier_list.domain.model.TierListItem
import com.ulyup.tier_list.domain.repository.ItemRepository
import com.ulyup.tier_list.model.Tier
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

    override suspend fun create(tierListId: Int, userId: Int, imageUrl: String): TierListItem? = dbQuery {
        if (!ownsParent(tierListId, userId)) return@dbQuery null
        val position = loadTier(tierListId, tier = null).size
        val newId = TierListItemsTable.insert {
            it[TierListItemsTable.tierListId] = tierListId
            it[TierListItemsTable.imageUrl] = imageUrl
            it[TierListItemsTable.tier] = null
            it[TierListItemsTable.position] = position
        } get TierListItemsTable.id
        TierListItem(newId, tierListId, imageUrl, tier = null, position = position)
    }

    override suspend fun insertCopy(tierListId: Int, imageUrl: String, tier: Tier?, position: Int): TierListItem = dbQuery {
        val newId = TierListItemsTable.insert {
            it[TierListItemsTable.tierListId] = tierListId
            it[TierListItemsTable.imageUrl] = imageUrl
            it[TierListItemsTable.tier] = tier
            it[TierListItemsTable.position] = position
        } get TierListItemsTable.id
        TierListItem(newId, tierListId, imageUrl, tier, position)
    }

    override suspend fun findById(id: Int): TierListItem? = dbQuery { fetchById(id) }

    override suspend fun findByTierListId(tierListId: Int): List<TierListItem> = dbQuery {
        TierListItemsTable.selectAll()
            .where { TierListItemsTable.tierListId eq tierListId }
            .orderBy(TierListItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }
    }

    override suspend fun update(id: Int, tierListId: Int, userId: Int, imageUrl: String): TierListItem? = dbQuery {
        if (!ownsParent(tierListId, userId)) return@dbQuery null
        val count = TierListItemsTable.update({
            (TierListItemsTable.id eq id) and (TierListItemsTable.tierListId eq tierListId)
        }) {
            it[TierListItemsTable.imageUrl] = imageUrl
        }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun delete(id: Int, tierListId: Int, userId: Int): TierListItem? = dbQuery {
        if (!ownsParent(tierListId, userId)) return@dbQuery null
        val item = fetchById(id) ?: return@dbQuery null
        if (item.tierListId != tierListId) return@dbQuery null
        TierListItemsTable.deleteWhere { TierListItemsTable.id eq id }
        compactTier(tierListId, item.tier)
        item
    }

    override suspend fun deleteByTierListId(tierListId: Int, userId: Int): List<TierListItem>? = dbQuery {
        if (!ownsParent(tierListId, userId)) return@dbQuery null
        val items = TierListItemsTable.selectAll()
            .where { TierListItemsTable.tierListId eq tierListId }
            .map { it.toItem() }
        TierListItemsTable.deleteWhere { TierListItemsTable.tierListId eq tierListId }
        items
    }

    override suspend fun move(id: Int, tierListId: Int, userId: Int, newTier: Tier?, newPosition: Int): TierListItem? = dbQuery {
        if (!ownsParent(tierListId, userId)) return@dbQuery null
        val item = fetchById(id) ?: return@dbQuery null
        if (item.tierListId != tierListId) return@dbQuery null

        val oldTier = item.tier
        val destination = loadTier(tierListId, newTier)
            .filter { it.id != id }
            .toMutableList()
            .also { it.add(newPosition.coerceIn(0, it.size), item) }
        assignPositions(newTier, destination)
        if (oldTier != newTier) compactTier(tierListId, oldTier)

        fetchById(id)
    }

    private fun ownsParent(tierListId: Int, userId: Int): Boolean =
        TierListsTable.selectAll()
            .where { (TierListsTable.id eq tierListId) and (TierListsTable.userId eq userId) }
            .count() > 0

    private fun loadTier(tierListId: Int, tier: Tier?): List<TierListItem> =
        TierListItemsTable.selectAll()
            .where { (TierListItemsTable.tierListId eq tierListId) and tierMatches(tier) }
            .orderBy(TierListItemsTable.position to SortOrder.ASC)
            .map { it.toItem() }

    private fun assignPositions(tier: Tier?, items: List<TierListItem>) {
        items.forEachIndexed { index, currentItem ->
            TierListItemsTable.update({ TierListItemsTable.id eq currentItem.id }) {
                it[TierListItemsTable.tier] = tier
                it[TierListItemsTable.position] = index
            }
        }
    }

    private fun compactTier(tierListId: Int, tier: Tier?) {
        loadTier(tierListId, tier).forEachIndexed { index, currentItem ->
            if (currentItem.position != index) {
                TierListItemsTable.update({ TierListItemsTable.id eq currentItem.id }) {
                    it[TierListItemsTable.position] = index
                }
            }
        }
    }

    private fun tierMatches(tier: Tier?): Op<Boolean> = TierListItemsTable.tier eq tier

    private fun fetchById(id: Int): TierListItem? =
        TierListItemsTable.selectAll().where { TierListItemsTable.id eq id }.singleOrNull()?.toItem()

    private fun ResultRow.toItem(): TierListItem = TierListItem(
        id = this[TierListItemsTable.id],
        tierListId = this[TierListItemsTable.tierListId],
        imageUrl = this[TierListItemsTable.imageUrl],
        tier = this[TierListItemsTable.tier],
        position = this[TierListItemsTable.position],
    )
}
