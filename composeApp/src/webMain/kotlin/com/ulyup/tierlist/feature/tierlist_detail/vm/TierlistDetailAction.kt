package com.ulyup.tierlist.feature.tierlist_detail.vm

import com.ulyup.tierlist.model.Tier

sealed interface TierlistDetailAction

data object LoadDetailAction : TierlistDetailAction
data object ShowAddItemDialogAction : TierlistDetailAction
data object DismissAddItemDialogAction : TierlistDetailAction
data object AddItemAction : TierlistDetailAction

@Suppress("ArrayInDataClass")
data class ImagePickedAction(
    val bytes: ByteArray,
    val filename: String,
) : TierlistDetailAction

value class DeleteItemAction(val itemId: Int) : TierlistDetailAction

data class MoveItemAction(
    val itemId: Int,
    val tier: Tier?,
    val position: Int,
) : TierlistDetailAction
