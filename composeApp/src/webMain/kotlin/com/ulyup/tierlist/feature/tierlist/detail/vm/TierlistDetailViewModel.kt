package com.ulyup.tierlist.feature.tierlist.detail.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.tierlist.usecase.CreateItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.DeleteItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetTierlistDetailUseCase
import com.ulyup.tierlist.domain.user.usecase.GetCurrentUserUseCase
import com.ulyup.tierlist.feature.tierlist.detail.mapper.applyDetail
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_add_error_url_blank
import kotlinx.coroutines.launch

class TierlistDetailViewModel(
    private val tierlistId: Int,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getTierlistDetailUseCase: GetTierlistDetailUseCase,
    private val createItemUseCase: CreateItemUseCase,
    private val deleteItemUseCase: DeleteItemUseCase,
) : StatefulViewModel<TierlistDetailAction, TierlistDetailState>(TierlistDetailState()) {

    init {
        viewModelScope.launch { load() }
    }

    override suspend fun handleAction(action: TierlistDetailAction) {
        when (action) {
            LoadDetailAction -> load()
            ShowAddItemDialogAction -> updateState { it.copy(addItemDialog = AddItemDialogState()) }
            DismissAddItemDialogAction -> updateState { it.copy(addItemDialog = null) }
            is ChangeAddItemUrlAction -> updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.copy(
                        url = action.value,
                        validationErrorRes = null,
                        serverErrorMessage = null,
                    ),
                )
            }
            AddItemAction -> addItem()
            is DeleteItemAction -> deleteItem(action.itemId)
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
            onError = { /* ownership defaults to false on any failure; load() surfaces the real error via getDetail */ },
        )
        return userId
    }

    private suspend fun addItem() {
        val dialog = state.addItemDialog ?: return
        if (dialog.isSubmitting) return
        val trimmedUrl = dialog.url.trim()
        if (trimmedUrl.isBlank()) {
            updateState { state ->
                state.copy(
                    addItemDialog = state.addItemDialog?.copy(
                        validationErrorRes = Res.string.detail_add_error_url_blank,
                    ),
                )
            }
            return
        }
        createItemUseCase(
            CreateItemUseCase.Params(tierlistId = tierlistId, imageUrl = trimmedUrl)
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
                unrankedItems = state.unrankedItems.filterNot { it.id == itemId },
                itemsByTier = state.itemsByTier.mapValues { (_, items) ->
                    items.filterNot { it.id == itemId }
                },
            )
        }
        deleteItemUseCase(
            DeleteItemUseCase.Params(tierlistId = tierlistId, itemId = itemId)
        ).fold(
            onLoading = { },
            onSuccess = { },
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
}
