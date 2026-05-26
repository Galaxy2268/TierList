package com.ulyup.tier_list.feature.profile.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.auth.usecase.LogoutUseCase
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.domain.user.usecase.UpgradePremiumUseCase
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val upgradePremiumUseCase: UpgradePremiumUseCase,
    private val logoutUseCase: LogoutUseCase,
) : InteractiveStatefulViewModel<ProfileAction, ProfileState, ProfileEvent>(ProfileState()) {

    init {
        viewModelScope.launch {
            observeCurrentUserUseCase(Unit).collect { user ->
                updateState { it.copy(user = user) }
            }
        }
    }

    override suspend fun handleAction(action: ProfileAction) {
        when (action) {
            ShowLogoutConfirmAction -> updateState { it.copy(showLogoutConfirm = true) }
            DismissLogoutConfirmAction -> updateState { it.copy(showLogoutConfirm = false) }
            ConfirmLogoutAction -> logout()
            UpgradePremiumAction -> upgrade()
        }
    }

    private suspend fun logout() {
        if (state.isLoggingOut) return
        updateState { it.copy(showLogoutConfirm = false) }
        logoutUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isLoggingOut = true) } },
            onSuccess = { updateState { it.copy(isLoggingOut = false) } },
            onError = { updateState { it.copy(isLoggingOut = false) } },
        )
    }

    private suspend fun upgrade() {
        if (state.isUpgrading) return
        upgradePremiumUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isUpgrading = true) } },
            onSuccess = { updateState { it.copy(isUpgrading = false) } },
            onError = { exception ->
                updateState { it.copy(isUpgrading = false) }
                launchEvent(ShowErrorMessageEvent(exception.message))
            },
        )
    }
}
