package com.ulyup.tier_list.data.db.tables

import com.ulyup.tier_list.model.Tier
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object TierListItemsTable : Table("tier_list_items") {
    val id = integer("id").autoIncrement()
    val tierListId = integer("tier_list_id")
        .references(TierListsTable.id, onDelete = ReferenceOption.CASCADE)
    val imageUrl = varchar("image_url", 2048)
    val tier = enumerationByName("tier", 1, Tier::class).nullable()
    val position = integer("position")

    override val primaryKey = PrimaryKey(id)
}