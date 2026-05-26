package com.ulyup.tier_list.feature.tier_list_detail.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.usecase.CreateItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetTierListDetailUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.MoveItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetTierListVisibilityUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.UpdateTierListUseCase
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.feature.tier_list_detail.mapper.applyDetail
import com.ulyup.tier_list.feature.tier_list_detail.util.findItem
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_add_error_no_image
import com.ulyup.tier_list.resources.detail_rename_error_title_blank
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class TierListDetailViewModel(
    private val tierListId: Int,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getTierListDetailUseCase: GetTierListDetailUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val moveItemUseCase: MoveItemUseCase,
    private val updateTierListUseCase: UpdateTierListUseCase,
    private val setTierListVisibilityUseCase: SetTierListVisibilityUseCase,
    private val deleteTierListUseCase: DeleteTierListUseCase,
) : InteractiveStatefulViewModel<TierListDetailAction, TierListDetailState, TierListDetailEvent>(
    TierListDetailState()
) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: TierListDetailAction) {
        when (action) {
            LoadDetailAction -> load()
            ShowAddItemDialogAction -> updateState { it.copy(addItemDialog = AddItemDialogState()) }
            DismissAddItemDialogAction -> updateState { it.copy(addItemDialog = null) }
            is ImagePickedAction -> updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog
                        ?.copy(pickedImage = PickedImage(action.bytes, action.filename))
                        ?.withInputChanged(),
                )
            }
            AddItemAction -> addItem()
            is DeleteItemAction -> deleteItem(action.itemId)
            is MoveItemAction -> moveItem(action.itemId, action.tier, action.position)
            ShowRenameDialogAction -> updateState {
                it.copy(renameDialog = RenameDialogState(title = it.title))
            }
            DismissRenameDialogAction -> updateState { it.copy(renameDialog = null) }
            is ChangeRenameTitleAction -> updateRenameDialog {
                it.copy(title = action.value).withInputChanged()
            }
            ConfirmRenameAction -> confirmRename()
            ToggleVisibilityAction -> toggleVisibility()
            ShowDeleteConfirmAction -> updateState {
                it.copy(isDeleteConfirmVisible = true, deleteErrorMessage = null)
            }
            DismissDeleteConfirmAction -> updateState {
                it.copy(isDeleteConfirmVisible = false, deleteErrorMessage = null)
            }
            ConfirmDeleteAction -> confirmDelete()
            ShareAction -> share()
            DismissSharePrivateWarningAction -> updateState {
                it.copy(showSharePrivateWarning = false)
            }
        }
    }

    private suspend fun share() {
        sendEvent(ShareLinkCopiedEvent)
        if (state.isOwner && !state.isPublic) {
            updateState { it.copy(showSharePrivateWarning = true) }
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        val currentUserId = observeCurrentUserUseCase(Unit).firstOrNull()?.id
        getTierListDetailUseCase(tierListId).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { detail -> updateState { it.applyDetail(detail, currentUserId) } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }

    private suspend fun addItem() {
        val dialog = state.addItemDialog ?: return
        if (dialog.isLoading) return
        val picked = dialog.pickedImage
        if (picked == null) {
            updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.withValidationError(Res.string.detail_add_error_no_image),
                )
            }
            return
        }
        createItemUseCase(
            CreateItemUseCase.Params(
                tierListId = tierListId,
                bytes = picked.bytes,
                filename = picked.filename,
            )
        ).fold(
            onLoading = {
                updateState { state ->
                    state.copy(addItemDialog = state.addItemDialog?.withLoading())
                }
            },
            onSuccess = { created ->
                updateState {
                    it.copy(
                        addItemDialog = null,
                        unrankedItems = it.unrankedItems + created,
                    )
                }
            },
            onError = { exception ->
                updateState { state ->
                    state.copy(addItemDialog = state.addItemDialog?.withError(exception.message))
                }
            },
        )
    }

    private suspend fun deleteItem(itemId: Int) {
        val unrankedSnapshot = state.unrankedItems
        val itemsByTierSnapshot = state.itemsByTier
        updateState { state ->
            state.copy(
                unrankedItems = state.unrankedItems.filterNot { it.id == itemId }.compactPositions(),
                itemsByTier = state.itemsByTier.mapValues { (_, items) ->
                    items.filterNot { it.id == itemId }.compactPositions()
                },
            )
        }
        deleteItemUseCase(
            DeleteItemUseCase.Params(tierListId = tierListId, itemId = itemId)
        ).fold(
            onError = { exception ->
                updateState { current ->
                    current.copy(
                        unrankedItems = unrankedSnapshot,
                        itemsByTier = itemsByTierSnapshot,
                    )
                }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }

    private suspend fun moveItem(itemId: Int, targetTier: Tier?, targetPosition: Int) {
        val item = state.findItem(itemId) ?: return
        if (item.tier == targetTier && item.position == targetPosition) return
        val unrankedSnapshot = state.unrankedItems
        val itemsByTierSnapshot = state.itemsByTier

        updateState { current ->
            val movedItem = item.copy(tier = targetTier, position = targetPosition)
            val sourceTier = item.tier

            val byTierAfterRemove: Map<Tier, List<TierListItem>> = if (sourceTier != null) {
                val sourceList = current.itemsByTier[sourceTier].orEmpty()
                    .filterNot { it.id == itemId }
                    .compactPositions()
                current.itemsByTier + (sourceTier to sourceList)
            } else current.itemsByTier

            val unrankedAfterRemove: List<TierListItem> = if (sourceTier == null) {
                current.unrankedItems.filterNot { it.id == itemId }.compactPositions()
            } else current.unrankedItems

            if (targetTier == null) {
                current.copy(
                    itemsByTier = byTierAfterRemove,
                    unrankedItems = unrankedAfterRemove.insertAt(targetPosition, movedItem).compactPositions(),
                )
            } else {
                val newTargetList = byTierAfterRemove[targetTier].orEmpty()
                    .insertAt(targetPosition, movedItem)
                    .compactPositions()
                current.copy(
                    unrankedItems = unrankedAfterRemove,
                    itemsByTier = byTierAfterRemove + (targetTier to newTargetList),
                )
            }
        }

        moveItemUseCase(
            MoveItemUseCase.Params(
                tierListId = tierListId,
                itemId = itemId,
                tier = targetTier,
                position = targetPosition,
            )
        ).fold(
            onError = { exception ->
                updateState { now ->
                    now.copy(
                        unrankedItems = unrankedSnapshot,
                        itemsByTier = itemsByTierSnapshot,
                    )
                }
                launchEvent(ShowErrorMessageEvent(exception.message))
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
            UpdateTierListUseCase.Params(id = tierListId, title = newTitle)
        ).fold(
            onLoading = { updateRenameDialog { it.withLoading() } },
            onSuccess = { updated ->
                updateState { it.copy(title = updated.title, renameDialog = null) }
            },
            onError = { exception -> updateRenameDialog { it.withError(exception.message) } },
        )
    }

    private suspend fun toggleVisibility() {
        if (state.isUpdatingVisibility) return
        val target = !state.isPublic
        setTierListVisibilityUseCase(
            SetTierListVisibilityUseCase.Params(id = tierListId, isPublic = target)
        ).fold(
            onLoading = { updateState { it.copy(isUpdatingVisibility = true) } },
            onSuccess = { updated ->
                updateState { it.copy(isUpdatingVisibility = false, isPublic = updated.isPublic) }
            },
            onError = { exception ->
                updateState { it.copy(isUpdatingVisibility = false) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }

    private suspend fun confirmDelete() {
        if (state.isDeleting) return
        updateState { it.copy(isDeleting = true, deleteErrorMessage = null) }
        deleteTierListUseCase(tierListId).fold(
            onSuccess = {
                updateState { it.copy(isDeleteConfirmVisible = false, isDeleting = false) }
                launchEvent(TierListDeletedEvent)
            },
            onError = { exception ->
                updateState {
                    it.copy(isDeleting = false, deleteErrorMessage = exception.message)
                }
            },
        )
    }

    private fun updateRenameDialog(reducer: (RenameDialogState) -> RenameDialogState) {
        updateState { state ->
            val current = state.renameDialog ?: return@updateState state
            state.copy(renameDialog = reducer(current))
        }
    }

    private fun List<TierListItem>.insertAt(index: Int, item: TierListItem): List<TierListItem> {
        val safeIndex = index.coerceIn(0, size)
        return toMutableList().also { it.add(safeIndex, item) }
    }

    private fun List<TierListItem>.compactPositions(): List<TierListItem> =
        mapIndexed { idx, item -> if (item.position == idx) item else item.copy(position = idx) }
}
