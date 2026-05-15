package com.ulyup.tierlist.data.db

import com.ulyup.tierlist.data.db.tables.TierlistItemsTable
import com.ulyup.tierlist.data.db.tables.TierlistsTable
import com.ulyup.tierlist.data.db.tables.UsersTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

object DatabaseFactory {
    fun init() {
        File("./data").mkdirs()
        Database.connect("jdbc:sqlite:./data/tierrank.db", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(UsersTable, TierlistsTable, TierlistItemsTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        suspendTransaction { block() }
}