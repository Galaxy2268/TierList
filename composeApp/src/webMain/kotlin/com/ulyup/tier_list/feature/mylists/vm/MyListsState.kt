package com.ulyup.tier_list.feature.mylists.vm

import com.ulyup.tier_list.FREE_TIER_LIMIT
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
    val isSubmitting: Boolean = false,
    val validationErrorRes: StringResource? = null,
    val serverErrorMessage: String? = null,
)