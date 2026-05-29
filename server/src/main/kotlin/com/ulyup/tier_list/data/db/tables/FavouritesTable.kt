package com.ulyup.tier_list.data.db.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object FavouritesTable : Table("favourites") {
    val userId = integer("user_id")
        .references(UsersTable.id, onDelete = ReferenceOption.CASCADE)
    val tierListId = integer("tier_list_id")
        .references(TierListsTable.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(userId, tierListId)
}
