package com.ulyup.tier_list.feature.tier_list_detail.vm

import com.ulyup.tier_list.core.mvi.FormState
import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.core.ui.components.button.model.TierListOption
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier
import org.jetbrains.compose.resources.StringResource
import kotlin.random.Random

data class TierListDetailState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val title: String = "",
    val isPublic: Boolean = false,
    val isOwner: Boolean = false,
    val isFavourite: Boolean = false,
    val itemsByTier: Map<Tier, List<TierListItem>> = emptyMap(),
    val unrankedItems: List<TierListItem> = emptyList(),
    val addItemDialog: AddItemDialogState? = null,
) : LoadableState<TierListDetailState> {

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)

    val options: List<TierListOption>
        get() = TierListOption.getVisibleOptions(isOwner)
}

data class AddItemDialogState(
    val pickedImages: List<PickedImage> = emptyList(),
    override val isLoading: Boolean = false,
    override val validationErrorRes: StringResource? = null,
    override val errorMessage: String? = null,
) : FormState<AddItemDialogState> {
    val isSubmitEnabled: Boolean
        get() = pickedImages.isNotEmpty()

    override fun copyForm(
        isLoading: Boolean,
        validationErrorRes: StringResource?,
        errorMessage: String?,
    ) = copy(
        isLoading = isLoading,
        validationErrorRes = validationErrorRes,
        errorMessage = errorMessage,
    )
}

@Suppress("ArrayInDataClass")
data class PickedImage(
    val bytes: ByteArray,
    val filename: String,
    val id: Long = Random.nextLong(),
)
