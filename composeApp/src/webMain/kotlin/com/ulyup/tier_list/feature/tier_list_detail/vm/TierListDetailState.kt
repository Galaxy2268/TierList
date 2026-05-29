package com.ulyup.tier_list.feature.tier_list_detail.vm

import com.ulyup.tier_list.core.mvi.FormState
import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.core.ui.components.button.model.ActionVisibility
import com.ulyup.tier_list.core.ui.components.button.model.TierListAction
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier
import kotlin.random.Random
import org.jetbrains.compose.resources.StringResource

data class TierListDetailState(
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
    val title: String = "",
    val isPublic: Boolean = false,
    val isOwner: Boolean = false,
    val isLoggedIn: Boolean = false,
    val isFavourite: Boolean = false,
    val isUpdatingVisibility: Boolean = false,
    val itemsByTier: Map<Tier, List<TierListItem>> = emptyMap(),
    val unrankedItems: List<TierListItem> = emptyList(),
    val addItemDialog: AddItemDialogState? = null,
    val renameDialog: RenameDialogState? = null,
    val isDeleteConfirmVisible: Boolean = false,
    val isDeleting: Boolean = false,
    val deleteErrorMessage: String? = null,
    val isClearConfirmVisible: Boolean = false,
    val isClearing: Boolean = false,
    val clearErrorMessage: String? = null,
    val showSharePrivateWarning: Boolean = false,
) : LoadableState<TierListDetailState> {

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)

    val actions: List<TierListAction>
        get() = TierListAction.entries.filter { action ->
            when (action.visibility) {
                ActionVisibility.EVERYONE -> true
                ActionVisibility.LOGGED_IN -> isLoggedIn
                ActionVisibility.OWNER_ONLY -> isOwner
            }
        }
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

data class RenameDialogState(
    val title: String = "",
    override val isLoading: Boolean = false,
    override val validationErrorRes: StringResource? = null,
    override val errorMessage: String? = null,
) : FormState<RenameDialogState> {
    val isSubmitEnabled: Boolean
        get() = title.isNotBlank()

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
