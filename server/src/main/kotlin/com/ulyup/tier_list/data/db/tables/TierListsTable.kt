package com.ulyup.tier_list.data.db.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object TierListsTable : Table("tier_lists") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id")
        .references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 120)
    val isPublic = bool("is_public").default(false)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}