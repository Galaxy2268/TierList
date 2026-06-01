package com.ulyup.tier_list.feature.mylists.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.util.favouritesFirst
import com.ulyup.tier_list.domain.tier_list.util.mapById
import com.ulyup.tier_list.domain.tier_list.util.removeById
import com.ulyup.tier_list.domain.tier_list.util.setFavourite
import com.ulyup.tier_list.domain.tier_list.util.setTitle
import com.ulyup.tier_list.domain.tier_list.usecase.CreateTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetMyTierListsUseCase
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.domain.user.usecase.UpgradePremiumUseCase
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.mylists_create_error_title_blank
import kotlinx.coroutines.launch

class MyListsViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getMyTierListsUseCase: GetMyTierListsUseCase,
    private val createTierListUseCase: CreateTierListUseCase,
    private val upgradePremiumUseCase: UpgradePremiumUseCase,
) : InteractiveStatefulViewModel<MyListsAction, MyListsState, MyListsEvent>(MyListsState()) {

    init {
        viewModelScope.launch {
            observeCurrentUserUseCase(Unit).collect { user ->
                updateState { it.copy(userRole = user?.role) }
            }
        }
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: MyListsAction) {
        when (action) {
            LoadMyListsAction -> load()
            ShowCreateDialogAction -> showCreateDialog()
            DismissCreateDialogAction -> updateState { it.copy(createDialog = null) }
            is ChangeCreateTitleAction -> updateCreateDialog {
                it.copy(title = action.value).withInputChanged()
            }
            is ToggleCreatePublicAction -> updateCreateDialog { it.copy(isPublic = action.value) }
            ConfirmCreateAction -> confirmCreate()
            UpgradePremiumAction -> upgrade()
            is RemoveTierListAction -> updateState {
                it.copy(tierLists = it.tierLists.removeById(action.tierListId))
            }
            is SetFavouriteAction -> updateState {
                it.copy(tierLists = it.tierLists.setFavourite(action.tierListId, action.isFavourite))
            }
            is SetTitleAction -> updateState {
                it.copy(tierLists = it.tierLists.setTitle(action.tierListId, action.title))
            }
            is SetVisibilityAction -> updateState {
                it.copy(
                    tierLists = it.tierLists.mapById(action.tierListId) { tierList ->
                        tierList.copy(isPublic = action.isPublic)
                    },
                )
            }
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getMyTierListsUseCase(Unit).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { tierLists ->
                updateState { it.withSuccess().copy(tierLists = tierLists) }
            },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }

    private fun showCreateDialog() {
        if (state.isAtCap) return
        updateState { it.copy(createDialog = CreateDialogState()) }
    }

    private suspend fun confirmCreate() {
        val dialog = state.createDialog ?: return
        if (dialog.isLoading) return
        if (dialog.title.isBlank()) {
            updateCreateDialog { it.withValidationError(Res.string.mylists_create_error_title_blank) }
            return
        }
        createTierListUseCase(
            CreateTierListUseCase.Params(
                title = dialog.title.trim(),
                isPublic = dialog.isPublic,
            )
        ).fold(
            onLoading = { updateCreateDialog { it.withLoading() } },
            onSuccess = { created ->
                updateState {
                    it.copy(
                        tierLists = (listOf(created) + it.tierLists).favouritesFirst(),
                        createDialog = null,
                    )
                }
            },
            onError = { exception -> updateCreateDialog { it.withError(exception.message) } },
        )
    }

    private suspend fun upgrade() {
        if (state.isUpgrading) return
        upgradePremiumUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isUpgrading = true) } },
            onSuccess = { updateState { it.copy(isUpgrading = false) } },
            onError = { exception ->
                updateState { it.copy(isUpgrading = false) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }

    private fun updateCreateDialog(reducer: (CreateDialogState) -> CreateDialogState) {
        updateState { state ->
            val current = state.createDialog ?: return@updateState state
            state.copy(createDialog = reducer(current))
        }
    }
}
