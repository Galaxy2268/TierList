package com.ulyup.tierlist.data.db.tables

import com.ulyup.tierlist.model.UserRole
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 32).uniqueIndex()
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 72)
    val role = enumerationByName("role", 16, UserRole::class)
    val createdAt = timestamp("created_at")

    override val primaryKey = PrimaryKey(id)
}