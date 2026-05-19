package com.ulyup.tierlist.feature.mylists.vm

import com.ulyup.tierlist.FREE_TIER_LIMIT
import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.model.UserRole
import org.jetbrains.compose.resources.StringResource

data class MyListsState(
    val isLoading: Boolean = false,
    val tierlists: List<Tierlist> = emptyList(),
    val userRole: UserRole? = null,
    val errorMessage: String? = null,
    val createDialog: CreateDialogState? = null,
    val isUpgrading: Boolean = false,
) {
    val isAtCap: Boolean
        get() = userRole == UserRole.USER && tierlists.size >= FREE_TIER_LIMIT

    val isInitialLoad: Boolean
        get() = userRole == null && tierlists.isEmpty()

    val showCreateFab: Boolean
        get() = userRole != null && !isAtCap && errorMessage == null
}

data class CreateDialogState(
    val title: String = "",
    val isPublic: Boolean = false,
    val isSubmitting: Boolean = false,
    val validationErrorRes: StringResource? = null,
    val serverErrorMessage: String? = null,
)
