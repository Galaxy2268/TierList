package com.ulyup.tier_list.feature.mylists.vm

import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.model.TierListSort

sealed interface MyListsAction

data object LoadMyListsAction : MyListsAction
data object ShowCreateDialogAction : MyListsAction
data object DismissCreateDialogAction : MyListsAction

value class ChangeCreateTitleAction(val value: String) : MyListsAction
value class ToggleCreatePublicAction(val value: Boolean) : MyListsAction

data object ConfirmCreateAction : MyListsAction
data object UpgradePremiumAction : MyListsAction

data class AddTierListAction(val tierList: TierList) : MyListsAction
value class RemoveTierListAction(val tierListId: Int) : MyListsAction
data class SetFavouriteAction(val tierListId: Int, val isFavourite: Boolean) : MyListsAction
data class SetTitleAction(val tierListId: Int, val title: String) : MyListsAction
data class SetVisibilityAction(val tierListId: Int, val isPublic: Boolean) : MyListsAction

value class ChangeSearchQueryAction(val value: String) : MyListsAction
value class ChangeSortOrderAction(val sort: TierListSort) : MyListsAction
data object ToggleFavouritesOnlyAction : MyListsAction
