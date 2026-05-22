package com.ulyup.tierlist.feature.tierlist.detail.vm

import com.ulyup.tierlist.core.mvi.LoadableState
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.model.Tier
import org.jetbrains.compose.resources.StringResource

data class TierlistDetailState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val title: String = "",
    val isOwner: Boolean = false,
    val itemsByTier: Map<Tier, List<Item>> = emptyMap(),
    val unrankedItems: List<Item> = emptyList(),
    val addItemDialog: AddItemDialogState? = null,
) : LoadableState<TierlistDetailState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class AddItemDialogState(
    val url: String = "",
    val isSubmitting: Boolean = false,
    val validationErrorRes: StringResource? = null,
    val serverErrorMessage: String? = null,
)
