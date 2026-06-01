package com.ulyup.tier_list.feature.shared.tier_list.vm

import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.usecase.ClearItemsUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetFavouriteUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetTierListVisibilityUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.UpdateTierListUseCase
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_rename_error_title_blank

class TierListOptionsViewModel(
    private val deleteTierListUseCase: DeleteTierListUseCase,
    private val clearItemsUseCase: ClearItemsUseCase,
    private val updateTierListUseCase: UpdateTierListUseCase,
    private val setTierListVisibilityUseCase: SetTierListVisibilityUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase,
) : InteractiveStatefulViewModel<TierListOptionsAction, TierListOptionsState, TierListOptionsEvent>(
    TierListOptionsState()
) {

    override suspend fun handleAction(action: TierListOptionsAction) {
        when (action) {
            is ShowDeleteAction -> updateState {
                it.copy(deleteConfirm = DeleteConfirmState(action.target.id, action.target.title))
            }
            DismissDeleteAction -> updateState { it.copy(deleteConfirm = null) }
            ConfirmDeleteAction -> confirmDelete()

            is ShowClearAction -> updateState {
                it.copy(clearConfirm = ClearConfirmState(action.target.id))
            }
            DismissClearAction -> updateState { it.copy(clearConfirm = null) }
            ConfirmClearAction -> confirmClear()

            is ShowRenameAction -> updateState {
                it.copy(renameDialog = RenameDialogState(action.target.id, action.target.title))
            }
            DismissRenameAction -> updateState { it.copy(renameDialog = null) }
            is ChangeRenameTitleAction -> updateRenameDialog {
                it.copy(title = action.value).withInputChanged()
            }
            ConfirmRenameAction -> confirmRename()

            is ToggleVisibilityAction -> toggleVisibility(action.target)
            DismissSharePrivateWarningAction -> updateState { it.copy(sharePrivateWarning = null) }
            ConfirmMakePublicAction -> confirmMakePublic()

            is ToggleFavouriteAction -> toggleFavourite(action.target)

            is ShareAction -> share(action.target)
        }
    }

    private suspend fun confirmDelete() {
        val pending = state.deleteConfirm ?: return
        if (pending.isLoading) return
        updateState { it.copy(deleteConfirm = pending.withLoading()) }
        deleteTierListUseCase(pending.tierListId).fold(
            onSuccess = {
                updateState { it.copy(deleteConfirm = null) }
                launchEvent(TierListDeletedEvent(pending.tierListId))
            },
            onError = { exception ->
                updateState { it.copy(deleteConfirm = it.deleteConfirm?.withError(exception.message)) }
            },
        )
    }

    private suspend fun confirmClear() {
        val pending = state.clearConfirm ?: return
        if (pending.isLoading) return
        updateState { it.copy(clearConfirm = pending.withLoading()) }
        clearItemsUseCase(pending.tierListId).fold(
            onSuccess = {
                updateState { it.copy(clearConfirm = null) }
                launchEvent(ItemsClearedEvent(pending.tierListId))
            },
            onError = { exception ->
                updateState { it.copy(clearConfirm = it.clearConfirm?.withError(exception.message)) }
            },
        )
    }

    private suspend fun confirmRename() {
        val dialog = state.renameDialog ?: return
        if (dialog.isLoading) return
        val newTitle = dialog.title.trim()
        if (newTitle.isBlank()) {
            updateRenameDialog { it.withValidationError(Res.string.detail_rename_error_title_blank) }
            return
        }
        updateTierListUseCase(
            UpdateTierListUseCase.Params(id = dialog.tierListId, title = newTitle)
        ).fold(
            onLoading = { updateRenameDialog { it.withLoading() } },
            onSuccess = { updated ->
                updateState { it.copy(renameDialog = null) }
                launchEvent(TitleChangedEvent(dialog.tierListId, updated.title))
            },
            onError = { exception -> updateRenameDialog { it.withError(exception.message) } },
        )
    }

    private suspend fun toggleVisibility(target: TierListOptionTarget) {
        if (state.isUpdatingVisibility(target.id)) return
        setTierListVisibilityUseCase(
            SetTierListVisibilityUseCase.Params(id = target.id, isPublic = !target.isPublic)
        ).fold(
            onLoading = {
                updateState { it.copy(updatingVisibilityIds = it.updatingVisibilityIds + target.id) }
            },
            onSuccess = { updated ->
                updateState { it.copy(updatingVisibilityIds = it.updatingVisibilityIds - target.id) }
                launchEvent(VisibilityChangedEvent(target.id, updated.isPublic))
            },
            onError = { exception ->
                updateState { it.copy(updatingVisibilityIds = it.updatingVisibilityIds - target.id) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }

    private suspend fun confirmMakePublic() {
        val pending = state.sharePrivateWarning ?: return
        updateState { it.copy(sharePrivateWarning = null) }
        setTierListVisibilityUseCase(
            SetTierListVisibilityUseCase.Params(id = pending.tierListId, isPublic = true)
        ).fold(
            onSuccess = { updated -> launchEvent(VisibilityChangedEvent(pending.tierListId, updated.isPublic)) },
            onError = { exception -> launchEvent(ShowErrorMessageEvent(exception.message)) },
        )
    }

    private suspend fun toggleFavourite(target: TierListOptionTarget) {
        val newValue = !target.isFavourite
        sendEvent(FavouriteChangedEvent(target.id, newValue))
        setFavouriteUseCase(
            SetFavouriteUseCase.Params(tierListId = target.id, favourite = newValue)
        ).fold(
            onError = { exception ->
                launchEvent(FavouriteChangedEvent(target.id, target.isFavourite))
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }

    private suspend fun share(target: TierListOptionTarget) {
        sendEvent(ShareLinkCopiedEvent)
        if (target.isOwner && !target.isPublic) {
            updateState { it.copy(sharePrivateWarning = SharePrivateWarningState(target.id)) }
        }
    }

    private fun updateRenameDialog(reducer: (RenameDialogState) -> RenameDialogState) {
        updateState { state ->
            val current = state.renameDialog ?: return@updateState state
            state.copy(renameDialog = reducer(current))
        }
    }
}
