package com.ulyup.tier_list.feature.mylists.vm

sealed interface MyListsAction

data object LoadMyListsAction : MyListsAction
data object ShowCreateDialogAction : MyListsAction
data object DismissCreateDialogAction : MyListsAction

value class ChangeCreateTitleAction(val value: String) : MyListsAction
value class ToggleCreatePublicAction(val value: Boolean) : MyListsAction

data object ConfirmCreateAction : MyListsAction
data object UpgradePremiumAction : MyListsAction

value class RemoveTierListAction(val tierListId: Int) : MyListsAction
data class SetFavouriteAction(val tierListId: Int, val isFavourite: Boolean) : MyListsAction
data class SetTitleAction(val tierListId: Int, val title: String) : MyListsAction
data class SetVisibilityAction(val tierListId: Int, val isPublic: Boolean) : MyListsAction
