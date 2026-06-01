package com.ulyup.tier_list.feature.tier_list_detail.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.MAX_BATCH_IMAGES
import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.tier_list.model.ItemImage
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.usecase.CreateItemsBatchUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetTierListDetailUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.MoveItemUseCase
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.feature.tier_list_detail.mapper.applyDetail
import com.ulyup.tier_list.feature.tier_list_detail.util.findItem
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_add_error_no_image
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class TierListDetailViewModel(
    private val tierListId: Int,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val getTierListDetailUseCase: GetTierListDetailUseCase,
    private val createItemsBatchUseCase: CreateItemsBatchUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
    private val moveItemUseCase: MoveItemUseCase,
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
            is ImagesPickedAction -> onImagesPicked(action.images)
            is RemovePickedImageAction -> updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.let { dialog ->
                        if (action.index !in dialog.pickedImages.indices) dialog
                        else dialog.copy(
                            pickedImages = dialog.pickedImages.toMutableList()
                                .also { it.removeAt(action.index) },
                        ).withInputChanged()
                    },
                )
            }
            AddItemAction -> addItem()
            is DeleteItemAction -> deleteItem(action.itemId)
            is MoveItemAction -> moveItem(action.itemId, action.tier, action.position)
            is SetTitleAction -> updateState { it.copy(title = action.title) }
            is SetFavouriteAction -> updateState { it.copy(isFavourite = action.isFavourite) }
            is SetVisibilityAction -> updateState { it.copy(isPublic = action.isPublic) }
            ClearItemsAction -> updateState { it.copy(itemsByTier = emptyMap(), unrankedItems = emptyList()) }
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

    private fun onImagesPicked(images: List<PickedImage>) {
        val truncated = images.take(MAX_BATCH_IMAGES)
        updateState { state ->
            state.copy(
                addItemDialog = state.addItemDialog
                    ?.copy(pickedImages = truncated)
                    ?.withInputChanged(),
            )
        }
    }

    private suspend fun addItem() {
        val dialog = state.addItemDialog ?: return
        if (dialog.isLoading) return
        val picked = dialog.pickedImages
        if (picked.isEmpty()) {
            updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.withValidationError(Res.string.detail_add_error_no_image),
                )
            }
            return
        }
        val images = picked.map { ItemImage(bytes = it.bytes, filename = it.filename) }
        createItemsBatchUseCase(
            CreateItemsBatchUseCase.Params(tierListId = tierListId, images = images)
        ).fold(
            onLoading = {
                updateState { state -> state.copy(addItemDialog = state.addItemDialog?.withLoading()) }
            },
            onSuccess = { result ->
                if (result.created.isNotEmpty()) {
                    updateState {
                        it.copy(
                            addItemDialog = null,
                            unrankedItems = it.unrankedItems + result.created,
                        )
                    }
                } else {
                    updateState { state ->
                        state.copy(addItemDialog = state.addItemDialog?.copyLoadable(isLoading = false, errorMessage = null))
                    }
                }
                if (result.failedFilenames.isNotEmpty()) {
                    launchEvent(ShowUploadFailuresEvent(result.failedFilenames))
                }
            },
            onError = { exception ->
                updateState { state ->
                    state.copy(addItemDialog = state.addItemDialog?.copyLoadable(isLoading = false, errorMessage = null))
                }
                launchEvent(ShowErrorMessageEvent(exception.message))
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

    private fun List<TierListItem>.insertAt(index: Int, item: TierListItem): List<TierListItem> {
        val safeIndex = index.coerceIn(0, size)
        return toMutableList().also { it.add(safeIndex, item) }
    }

    private fun List<TierListItem>.compactPositions(): List<TierListItem> =
        mapIndexed { idx, item -> if (item.position == idx) item else item.copy(position = idx) }
}
