package com.ulyup.tier_list.feature.shared.tier_list.vm

import com.ulyup.tier_list.core.mvi.FormState
import com.ulyup.tier_list.core.mvi.LoadableState
import org.jetbrains.compose.resources.StringResource

data class TierListOptionsState(
    val deleteConfirm: DeleteConfirmState? = null,
    val clearConfirm: ClearConfirmState? = null,
    val renameDialog: RenameDialogState? = null,
    val sharePrivateWarning: SharePrivateWarningState? = null,
    val copyConfirm: CopyConfirmState? = null,
    val premiumLimit: PremiumLimitState? = null,
    val updatingVisibilityIds: Set<Int> = emptySet(),
) {
    fun isUpdatingVisibility(tierListId: Int): Boolean = tierListId in updatingVisibilityIds
}

data class DeleteConfirmState(
    val tierListId: Int,
    val title: String,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<DeleteConfirmState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class ClearConfirmState(
    val tierListId: Int,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<ClearConfirmState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class RenameDialogState(
    val tierListId: Int,
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

data class SharePrivateWarningState(
    val tierListId: Int,
)

data class CopyConfirmState(
    val target: TierListOptionTarget,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<CopyConfirmState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class PremiumLimitState(
    val target: TierListOptionTarget,
    val isUpgrading: Boolean = false,
)
