package com.ulyup.tierlist.data.repository

import com.ulyup.tierlist.data.db.DatabaseFactory.dbQuery
import com.ulyup.tierlist.data.db.tables.UsersTable
import com.ulyup.tierlist.domain.model.User
import com.ulyup.tierlist.domain.repository.UserRepository
import com.ulyup.tierlist.model.UserRole
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock

class UserRepositoryImpl : UserRepository {

    override suspend fun create(username: String, email: String, passwordHash: String): User = dbQuery {
        val now = Clock.System.now()
        val newId = UsersTable.insert {
            it[UsersTable.username] = username
            it[UsersTable.email] = email
            it[UsersTable.passwordHash] = passwordHash
            it[UsersTable.role] = UserRole.USER
            it[UsersTable.createdAt] = now
        } get UsersTable.id
        User(newId, username, email, passwordHash, UserRole.USER, now)
    }

    override suspend fun findByUsername(username: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.username eq username }
            .singleOrNull()?.toUser()
    }

    override suspend fun findByEmail(email: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .singleOrNull()?.toUser()
    }

    override suspend fun findByUsernameOrEmail(value: String): User? = dbQuery {
        UsersTable.selectAll()
            .where { (UsersTable.username eq value) or (UsersTable.email eq value) }
            .singleOrNull()?.toUser()
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        UsersTable.selectAll()
            .where { UsersTable.id eq id }
            .singleOrNull()?.toUser()
    }

    override suspend fun setRole(id: Int, role: UserRole): User? = dbQuery {
        val updated = UsersTable.update({ UsersTable.id eq id }) { it[UsersTable.role] = role }
        if (updated == 0) null
        else UsersTable.selectAll()
            .where { UsersTable.id eq id }
            .singleOrNull()?.toUser()
    }

    private fun ResultRow.toUser(): User = User(
        id = this[UsersTable.id],
        username = this[UsersTable.username],
        email = this[UsersTable.email],
        passwordHash = this[UsersTable.passwordHash],
        role = this[UsersTable.role],
        createdAt = this[UsersTable.createdAt],
    )
}