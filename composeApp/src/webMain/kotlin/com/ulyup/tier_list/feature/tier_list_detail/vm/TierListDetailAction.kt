package com.ulyup.tier_list.feature.tier_list_detail.vm

import com.ulyup.tier_list.model.Tier

sealed interface TierListDetailAction

data object LoadDetailAction : TierListDetailAction
data object ShowAddItemDialogAction : TierListDetailAction
data object DismissAddItemDialogAction : TierListDetailAction
data object AddItemAction : TierListDetailAction

data class ImagesPickedAction(
    val images: List<PickedImage>,
) : TierListDetailAction

value class RemovePickedImageAction(val index: Int) : TierListDetailAction

value class DeleteItemAction(val itemId: Int) : TierListDetailAction

data class MoveItemAction(
    val itemId: Int,
    val tier: Tier?,
    val position: Int,
) : TierListDetailAction

data object ShowRenameDialogAction : TierListDetailAction
data object DismissRenameDialogAction : TierListDetailAction
value class ChangeRenameTitleAction(val value: String) : TierListDetailAction
data object ConfirmRenameAction : TierListDetailAction

data object ToggleVisibilityAction : TierListDetailAction

data object ShowDeleteConfirmAction : TierListDetailAction
data object DismissDeleteConfirmAction : TierListDetailAction
data object ConfirmDeleteAction : TierListDetailAction

data object ShareAction : TierListDetailAction
data object DismissSharePrivateWarningAction : TierListDetailAction