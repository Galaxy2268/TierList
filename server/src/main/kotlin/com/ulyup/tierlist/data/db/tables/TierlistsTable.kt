package com.ulyup.tierlist.data.db.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object TierlistsTable : Table("tierlists") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
        .references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 120)
    val isPublic = bool("is_public").default(false)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}