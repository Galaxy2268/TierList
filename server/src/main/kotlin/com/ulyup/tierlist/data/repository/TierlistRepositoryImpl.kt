package com.ulyup.tierlist.data.repository

import com.ulyup.tierlist.data.db.DatabaseFactory.dbQuery
import com.ulyup.tierlist.data.db.tables.TierlistsTable
import com.ulyup.tierlist.domain.model.Tierlist
import com.ulyup.tierlist.domain.repository.TierlistRepository
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock

class TierlistRepositoryImpl : TierlistRepository {

    override suspend fun create(userId: Int, title: String, isPublic: Boolean): Tierlist = dbQuery {
        val now = Clock.System.now()
        val newId = TierlistsTable.insert {
            it[TierlistsTable.userId] = userId
            it[TierlistsTable.title] = title
            it[TierlistsTable.isPublic] = isPublic
            it[TierlistsTable.createdAt] = now
        } get TierlistsTable.id
        Tierlist(newId, userId, title, isPublic, now)
    }

    override suspend fun findById(id: Int): Tierlist? = dbQuery { fetchById(id) }

    override suspend fun findByUserId(userId: Int): List<Tierlist> = dbQuery {
        TierlistsTable.selectAll()
            .where { TierlistsTable.userId eq userId }
            .orderBy(TierlistsTable.createdAt to SortOrder.DESC)
            .map { it.toTierlist() }
    }

    override suspend fun listPublic(limit: Int, offset: Int): List<Tierlist> = dbQuery {
        TierlistsTable.selectAll()
            .where { TierlistsTable.isPublic eq true }
            .orderBy(TierlistsTable.createdAt to SortOrder.DESC)
            .limit(limit).offset(offset.toLong())
            .map { it.toTierlist() }
    }

    override suspend fun update(id: Int, title: String): Tierlist? = dbQuery {
        val count = TierlistsTable.update({ TierlistsTable.id eq id }) { it[TierlistsTable.title] = title }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun setVisibility(id: Int, isPublic: Boolean): Tierlist? = dbQuery {
        val count = TierlistsTable.update({ TierlistsTable.id eq id }) { it[TierlistsTable.isPublic] = isPublic }
        if (count == 0) null else fetchById(id)
    }

    override suspend fun delete(id: Int): Boolean = dbQuery {
        TierlistsTable.deleteWhere { TierlistsTable.id eq id } > 0
    }

    override suspend fun countByUser(userId: Int): Long = dbQuery {
        TierlistsTable.selectAll()
            .where { TierlistsTable.userId eq userId }
            .count()
    }

    private fun fetchById(id: Int): Tierlist? =
        TierlistsTable.selectAll().where { TierlistsTable.id eq id }.singleOrNull()?.toTierlist()

    private fun ResultRow.toTierlist(): Tierlist = Tierlist(
        id = this[TierlistsTable.id],
        userId = this[TierlistsTable.userId],
        title = this[TierlistsTable.title],
        isPublic = this[TierlistsTable.isPublic],
        createdAt = this[TierlistsTable.createdAt],
    )
}