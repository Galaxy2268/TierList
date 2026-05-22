package com.ulyup.tierlist.feature.tierlist_detail.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.domain.tierlist.usecase.CreateItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.DeleteItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetTierlistDetailUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.MoveItemUseCase
import com.ulyup.tierlist.domain.user.usecase.GetCurrentUserUseCase
import com.ulyup.tierlist.feature.tierlist_detail.mapper.applyDetail
import com.ulyup.tierlist.feature.tierlist_detail.util.findItem
import com.ulyup.tierlist.model.Tier
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_add_error_no_image
import kotlinx.coroutines.launch

class TierlistDetailViewModel(
    private val tierlistId: Int,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTierlistDetailUseCase: GetTierlistDetailUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val moveItemUseCase: MoveItemUseCase,
) : StatefulViewModel<TierlistDetailAction, TierlistDetailState>(TierlistDetailState()) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: TierlistDetailAction) {
        when (action) {
            LoadDetailAction -> load()
            ShowAddItemDialogAction -> updateState { it.copy(addItemDialog = AddItemDialogState()) }
            DismissAddItemDialogAction -> updateState { it.copy(addItemDialog = null) }
            is ImagePickedAction -> updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.copy(
                        pickedImage = PickedImage(action.bytes, action.filename),
                        validationErrorRes = null,
                        serverErrorMessage = null,
                    ),
                )
            }
            AddItemAction -> addItem()
            is DeleteItemAction -> deleteItem(action.itemId)
            is MoveItemAction -> moveItem(action.itemId, action.tier, action.position)
        }
    }

    private suspend fun load() {
        if (state.isLoading) return
        val currentUserId = fetchCurrentUserId()
        getTierlistDetailUseCase(tierlistId).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { detail -> updateState { it.applyDetail(detail, currentUserId) } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }

    private suspend fun fetchCurrentUserId(): Int? {
        var userId: Int? = null
        getCurrentUserUseCase(Unit).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { user -> userId = user.id },
        )
        return userId
    }

    private suspend fun addItem() {
        val dialog = state.addItemDialog ?: return
        if (dialog.isSubmitting) return
        val picked = dialog.pickedImage
        if (picked == null) {
            updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.copy(
                        validationErrorRes = Res.string.detail_add_error_no_image,
                    ),
                )
            }
            return
        }
        createItemUseCase(
            CreateItemUseCase.Params(
                tierlistId = tierlistId,
                bytes = picked.bytes,
                filename = picked.filename,
            )
        ).fold(
            onLoading = {
                updateState { state ->
                    state.copy(
                        addItemDialog = state.addItemDialog?.copy(
                            isSubmitting = true,
                            validationErrorRes = null,
                            serverErrorMessage = null,
                        ),
                    )
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
                    state.copy(
                        addItemDialog = state.addItemDialog?.copy(
                            isSubmitting = false,
                            serverErrorMessage = exception.message,
                        ),
                    )
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
            DeleteItemUseCase.Params(tierlistId = tierlistId, itemId = itemId)
        ).fold(
            onError = { exception ->
                updateState { current ->
                    current.copy(
                        unrankedItems = unrankedSnapshot,
                        itemsByTier = itemsByTierSnapshot,
                        errorMessage = exception.message,
                    )
                }
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

            val byTierAfterRemove: Map<Tier, List<Item>> = if (sourceTier != null) {
                val sourceList = current.itemsByTier[sourceTier].orEmpty()
                    .filterNot { it.id == itemId }
                    .compactPositions()
                current.itemsByTier + (sourceTier to sourceList)
            } else current.itemsByTier

            val unrankedAfterRemove: List<Item> = if (sourceTier == null) {
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
                tierlistId = tierlistId,
                itemId = itemId,
                tier = targetTier,
                position = targetPosition,
            )
        ).fold(
            onLoading = { },
            onSuccess = { },
            onError = { exception ->
                updateState { now ->
                    now.copy(
                        unrankedItems = unrankedSnapshot,
                        itemsByTier = itemsByTierSnapshot,
                        errorMessage = exception.message,
                    )
                }
            },
        )
    }

    private fun List<Item>.insertAt(index: Int, item: Item): List<Item> {
        val safeIndex = index.coerceIn(0, size)
        return toMutableList().also { it.add(safeIndex, item) }
    }

    private fun List<Item>.compactPositions(): List<Item> =
        mapIndexed { idx, item -> if (item.position == idx) item else item.copy(position = idx) }
}
