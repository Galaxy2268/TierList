package com.ulyup.tier_list.feature.profile.vm

import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.model.UserRole

data class ProfileState(
    val user: User? = null,
    val isLoggingOut: Boolean = false,
    val isUpgrading: Boolean = false,
    val errorMessage: String? = null,
    val showLogoutConfirm: Boolean = false,
) {
    val showUpgradeButton: Boolean
        get() = user?.role == UserRole.USER
}
