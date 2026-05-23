package com.ulyup.tier_list.feature.tier_list_detail.vm

import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier
import org.jetbrains.compose.resources.StringResource

data class TierListDetailState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val title: String = "",
    val isOwner: Boolean = false,
    val itemsByTier: Map<Tier, List<TierListItem>> = emptyMap(),
    val unrankedItems: List<TierListItem> = emptyList(),
    val addItemDialog: AddItemDialogState? = null,
) : LoadableState<TierListDetailState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class AddItemDialogState(
    val pickedImage: PickedImage? = null,
    val isSubmitting: Boolean = false,
    val validationErrorRes: StringResource? = null,
    val serverErrorMessage: String? = null,
)

@Suppress("ArrayInDataClass")
data class PickedImage(
    val bytes: ByteArray,
    val filename: String,
)
