package com.ulyup.tierlist.data.db.tables

import com.ulyup.tierlist.model.Tier
import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table

object TierlistItemsTable : Table("tierlist_items") {
    val id = integer("id").autoIncrement()
    val tierlistId = integer("tierlist_id")
        .references(TierlistsTable.id, onDelete = ReferenceOption.CASCADE)
    val imageUrl = varchar("image_url", 2048)
    val tier = enumerationByName("tier", 1, Tier::class).nullable()
    val position = integer("position")

    override val primaryKey = PrimaryKey(id)
}