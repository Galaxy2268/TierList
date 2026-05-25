package com.ulyup.tier_list.feature.mylists.vm

import com.ulyup.tier_list.FREE_TIER_LIMIT
import com.ulyup.tier_list.core.mvi.FormState
import com.ulyup.tier_list.core.mvi.LoadableState
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.model.UserRole
import org.jetbrains.compose.resources.StringResource

data class MyListsState(
    override val isLoading: Boolean = false,
    val tierLists: List<TierList> = emptyList(),
    val userRole: UserRole? = null,
    override val errorMessage: String? = null,
    val createDialog: CreateDialogState? = null,
    val deleteConfirm: DeleteConfirmState? = null,
    val isUpgrading: Boolean = false,
) : LoadableState<MyListsState> {
    val isAtCap: Boolean
        get() = userRole == UserRole.USER && tierLists.size >= FREE_TIER_LIMIT

    val showCreateFab: Boolean
        get() = userRole != null && !isAtCap && errorMessage == null

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}

data class CreateDialogState(
    val title: String = "",
    val isPublic: Boolean = false,
    override val isLoading: Boolean = false,
    override val validationErrorRes: StringResource? = null,
    override val errorMessage: String? = null,
) : FormState<CreateDialogState> {
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

data class DeleteConfirmState(
    val tierListId: Int,
    val title: String,
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<DeleteConfirmState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}
