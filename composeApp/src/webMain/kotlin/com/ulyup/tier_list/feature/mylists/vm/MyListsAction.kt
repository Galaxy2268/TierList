package com.ulyup.tier_list.feature.mylists.vm

import com.ulyup.tier_list.domain.tier_list.model.TierList

sealed interface MyListsAction

data object LoadMyListsAction : MyListsAction
data object ShowCreateDialogAction : MyListsAction
data object DismissCreateDialogAction : MyListsAction

value class ChangeCreateTitleAction(val value: String) : MyListsAction
value class ToggleCreatePublicAction(val value: Boolean) : MyListsAction

data object ConfirmCreateAction : MyListsAction
data object UpgradePremiumAction : MyListsAction

value class ShowDeleteTierListConfirmAction(val tierList: TierList) : MyListsAction
data object DismissDeleteTierListConfirmAction : MyListsAction
data object ConfirmDeleteTierListAction : MyListsAction
