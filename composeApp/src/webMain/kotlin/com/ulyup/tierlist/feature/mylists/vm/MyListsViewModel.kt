package com.ulyup.tierlist.feature.mylists.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.tierlist.usecase.CreateTierlistUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetMyTierlistsUseCase
import com.ulyup.tierlist.domain.user.usecase.GetCurrentUserUseCase
import com.ulyup.tierlist.domain.user.usecase.UpgradePremiumUseCase
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.mylists_create_error_title_blank
import kotlinx.coroutines.launch

class MyListsViewModel(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getMyTierlistsUseCase: GetMyTierlistsUseCase,
    private val createTierlistUseCase: CreateTierlistUseCase,
    private val upgradePremiumUseCase: UpgradePremiumUseCase,
) : StatefulViewModel<MyListsAction, MyListsState>(MyListsState()) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: MyListsAction) {
        when (action) {
            LoadMyListsAction -> load()
            ShowCreateDialogAction -> showCreateDialog()
            DismissCreateDialogAction -> updateState { it.copy(createDialog = null) }
            is ChangeCreateTitleAction -> updateCreateDialog {
                it.copy(title = action.value, validationErrorRes = null, serverErrorMessage = null)
            }
            is ToggleCreatePublicAction -> updateCreateDialog { it.copy(isPublic = action.value) }
            ConfirmCreateAction -> confirmCreate()
            UpgradePremiumAction -> upgrade()
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        getCurrentUserUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isLoading = true, errorMessage = null) } },
            onSuccess = { user -> updateState { it.copy(userRole = user.role) } },
            onError = { exception ->
                updateState { it.copy(isLoading = false, errorMessage = exception.message) }
            },
        )
        if (state.errorMessage != null) return
        getMyTierlistsUseCase(Unit).fold(
            onSuccess = { tierlists ->
                updateState { it.copy(isLoading = false, tierlists = tierlists) }
            },
            onError = { exception ->
                updateState { it.copy(isLoading = false, errorMessage = exception.message) }
            },
        )
    }

    private fun showCreateDialog() {
        if (state.isAtCap) return
        updateState { it.copy(createDialog = CreateDialogState()) }
    }

    private suspend fun confirmCreate() {
        val dialog = state.createDialog ?: return
        if (dialog.isSubmitting) return
        if (dialog.title.isBlank()) {
            updateCreateDialog {
                it.copy(validationErrorRes = Res.string.mylists_create_error_title_blank)
            }
            return
        }
        createTierlistUseCase(
            CreateTierlistUseCase.Params(
                title = dialog.title.trim(),
                isPublic = dialog.isPublic,
            )
        ).fold(
            onLoading = {
                updateCreateDialog {
                    it.copy(isSubmitting = true, validationErrorRes = null, serverErrorMessage = null)
                }
            },
            onSuccess = { created ->
                updateState {
                    it.copy(
                        tierlists = listOf(created) + it.tierlists,
                        createDialog = null,
                    )
                }
            },
            onError = { exception ->
                updateCreateDialog {
                    it.copy(isSubmitting = false, serverErrorMessage = exception.message)
                }
            },
        )
    }

    private suspend fun upgrade() {
        if (state.isUpgrading) return
        upgradePremiumUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isUpgrading = true, errorMessage = null) } },
            onSuccess = { user ->
                updateState { it.copy(isUpgrading = false, userRole = user.role) }
            },
            onError = { exception ->
                updateState { it.copy(isUpgrading = false, errorMessage = exception.message) }
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
