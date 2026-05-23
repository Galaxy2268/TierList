package com.ulyup.tier_list.feature.mylists.vm

sealed interface MyListsAction

data object LoadMyListsAction : MyListsAction
data object ShowCreateDialogAction : MyListsAction
data object DismissCreateDialogAction : MyListsAction

value class ChangeCreateTitleAction(val value: String) : MyListsAction
value class ToggleCreatePublicAction(val value: Boolean) : MyListsAction

data object ConfirmCreateAction : MyListsAction
data object UpgradePremiumAction : MyListsAction
