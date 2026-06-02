package com.ulyup.tier_list.feature.shared.tier_list.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.FREE_TIER_LIMIT
import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.error.ApiException
import com.ulyup.tier_list.domain.tier_list.usecase.ClearItemsUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.CopyTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetMyTierListsUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetFavouriteUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetTierListVisibilityUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.UpdateTierListUseCase
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.domain.user.usecase.UpgradePremiumUseCase
import com.ulyup.tier_list.model.UserRole
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.general_error_title_blank
import kotlinx.coroutines.launch

class TierListOptionsViewModel(
    private val deleteTierListUseCase: DeleteTierListUseCase,
    private val clearItemsUseCase: ClearItemsUseCase,
    private val updateTierListUseCase: UpdateTierListUseCase,
    private val setTierListVisibilityUseCase: SetTierListVisibilityUseCase,
    private val setFavouriteUseCase: SetFavouriteUseCase,
    private val copyTierListUseCase: CopyTierListUseCase,
    private val upgradePremiumUseCase: UpgradePremiumUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getMyTierListsUseCase: GetMyTierListsUseCase,
) : InteractiveStatefulViewModel<TierListOptionsAction, TierListOptionsState, TierListOptionsEvent>(
    TierListOptionsState()
) {

    private var currentRole: UserRole? = null

    init {
        viewModelScope.launch {
            observeCurrentUserUseCase(Unit).collect { currentRole = it?.role }
        }
    }

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

            is ShowCopyAction -> showCopy(action.target)
            DismissCopyAction -> updateState { it.copy(copyConfirm = null) }
            ConfirmCopyAction -> confirmCopy()
            DismissPremiumLimitAction -> updateState { it.copy(premiumLimit = null) }
            UpgradeAndCopyAction -> upgradeAndCopy()
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
            updateRenameDialog { it.withValidationError(Res.string.general_error_title_blank) }
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

    private suspend fun showCopy(target: TierListOptionTarget) {
        if (canCreateAnotherList()) {
            updateState { it.copy(copyConfirm = CopyConfirmState(target)) }
        } else {
            updateState { it.copy(premiumLimit = PremiumLimitState(target)) }
        }
    }

    private suspend fun canCreateAnotherList(): Boolean {
        if (currentRole == UserRole.PREMIUM) return true
        var ownedCount: Int? = null
        getMyTierListsUseCase(Unit).fold(
            onSuccess = { ownedCount = it.size },
        )
        // If the count couldn't be loaded, let the copy proceed; the server cap check is authoritative.
        return ownedCount?.let { it < FREE_TIER_LIMIT } ?: true
    }

    private suspend fun confirmCopy() {
        val pending = state.copyConfirm ?: return
        if (pending.isLoading) return
        updateState { it.copy(copyConfirm = pending.withLoading()) }
        performCopy(pending.target) { exception ->
            if (exception is ApiException.Forbidden) {
                // Client cap check was stale; the server rejected. Surface the upsell instead.
                updateState { it.copy(copyConfirm = null, premiumLimit = PremiumLimitState(pending.target)) }
            } else {
                updateState { it.copy(copyConfirm = it.copyConfirm?.withError(exception.message)) }
            }
        }
    }

    private suspend fun upgradeAndCopy() {
        val pending = state.premiumLimit ?: return
        if (pending.isUpgrading) return
        updateState { it.copy(premiumLimit = pending.copy(isUpgrading = true)) }
        var upgraded = false
        upgradePremiumUseCase(Unit).fold(
            onSuccess = { upgraded = true },
            onError = { exception ->
                updateState { it.copy(premiumLimit = null) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
        if (upgraded) {
            performCopy(pending.target) { exception ->
                updateState { it.copy(premiumLimit = null) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            }
        }
    }

    private suspend fun performCopy(
        target: TierListOptionTarget,
        onError: (ApiException) -> Unit,
    ) {
        copyTierListUseCase(target.id).fold(
            onSuccess = { created ->
                updateState { it.copy(copyConfirm = null, premiumLimit = null) }
                launchEvent(TierListCopiedEvent(created))
            },
            onError = onError,
        )
    }

    private fun updateRenameDialog(reducer: (RenameDialogState) -> RenameDialogState) {
        updateState { state ->
            val current = state.renameDialog ?: return@updateState state
            state.copy(renameDialog = reducer(current))
        }
    }
}
