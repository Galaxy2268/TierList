package com.ulyup.tierlist.feature.mylists.vm

sealed interface MyListsAction

data object LoadMyListsAction : MyListsAction
data object ShowCreateDialogAction : MyListsAction
data object DismissCreateDialogAction : MyListsAction
data class ChangeCreateTitleAction(val value: String) : MyListsAction
data class ToggleCreatePublicAction(val value: Boolean) : MyListsAction
data object ConfirmCreateAction : MyListsAction
data object UpgradePremiumAction : MyListsAction
