package com.ulyup.tier_list.data.db

import com.ulyup.tier_list.data.db.tables.FavouritesTable
import com.ulyup.tier_list.data.db.tables.TierListItemsTable
import com.ulyup.tier_list.data.db.tables.TierListsTable
import com.ulyup.tier_list.data.db.tables.UsersTable
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.suspendTransaction
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import java.io.File

object DatabaseFactory {
    fun init() {
        File("./data").mkdirs()
        Database.connect("jdbc:sqlite:./data/tierrank.db?foreign_keys=on", "org.sqlite.JDBC")
        transaction {
            SchemaUtils.create(UsersTable, TierListsTable, TierListItemsTable, FavouritesTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        suspendTransaction { block() }
}