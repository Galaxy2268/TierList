package com.ulyup.tier_list.data.repository

import com.ulyup.tier_list.data.db.DatabaseFactory.dbQuery
import com.ulyup.tier_list.data.db.tables.TierListsTable
import com.ulyup.tier_list.domain.model.TierList
import com.ulyup.tier_list.domain.repository.TierListRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock

class TierListRepositoryImpl : TierListRepository {

    override suspend fun create(userId: Int, title: String, isPublic: Boolean): TierList = dbQuery {
        val now = Clock.System.now()
        val newId = TierListsTable.insert {
            it[TierListsTable.userId] = userId
            it[TierListsTable.title] = title
            it[TierListsTable.isPublic] = isPublic
            it[TierListsTable.createdAt] = now
        } get TierListsTable.id
        TierList(newId, userId, title, isPublic, now)
    }

    override suspend fun findById(id: Int): TierList? = dbQuery { fetchById(id) }

    override suspend fun findByUserId(userId: Int): List<TierList> = dbQuery {
        TierListsTable.selectAll()
            .where { TierListsTable.userId eq userId }
            .orderBy(TierListsTable.createdAt to SortOrder.DESC)
            .map { it.toTierList() }
    }

    override suspend fun listPublic(): List<TierList> = dbQuery {
        TierListsTable.selectAll()
            .where { TierListsTable.isPublic eq true }
            .orderBy(TierListsTable.createdAt to SortOrder.DESC)
            .map { it.toTierList() }
    }

    override suspend fun update(id: Int, userId: Int, title: String): TierList? = dbQuery {
        val count = TierListsTable.update({ ownedBy(id, userId) }) { it[TierListsTable.title] = title }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun setVisibility(id: Int, userId: Int, isPublic: Boolean): TierList? = dbQuery {
        val count = TierListsTable.update({ ownedBy(id, userId) }) { it[TierListsTable.isPublic] = isPublic }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun delete(id: Int, userId: Int): Boolean = dbQuery {
        TierListsTable.deleteWhere { ownedBy(id, userId) } > 0
    }

    override suspend fun countByUser(userId: Int): Long = dbQuery {
        TierListsTable.selectAll()
            .where { TierListsTable.userId eq userId }
            .count()
    }

    private fun ownedBy(id: Int, userId: Int) =
        (TierListsTable.id eq id) and (TierListsTable.userId eq userId)

    private fun fetchById(id: Int): TierList? =
        TierListsTable.selectAll().where { TierListsTable.id eq id }.singleOrNull()?.toTierList()

    private fun ResultRow.toTierList(): TierList = TierList(
        id = this[TierListsTable.id],
        userId = this[TierListsTable.userId],
        title = this[TierListsTable.title],
        isPublic = this[TierListsTable.isPublic],
        createdAt = this[TierListsTable.createdAt],
    )
}
