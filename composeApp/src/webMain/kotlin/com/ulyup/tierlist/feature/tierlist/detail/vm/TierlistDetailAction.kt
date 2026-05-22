package com.ulyup.tierlist.feature.tierlist.detail.vm

sealed interface TierlistDetailAction

data object LoadDetailAction : TierlistDetailAction
data object ShowAddItemDialogAction : TierlistDetailAction
data object DismissAddItemDialogAction : TierlistDetailAction
data object AddItemAction : TierlistDetailAction

value class ChangeAddItemUrlAction(val value: String) : TierlistDetailAction
value class DeleteItemAction(val itemId: Int) : TierlistDetailAction
