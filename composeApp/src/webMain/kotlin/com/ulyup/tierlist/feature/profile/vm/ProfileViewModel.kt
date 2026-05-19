package com.ulyup.tierlist.feature.profile.vm

import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.auth.usecase.LogoutUseCase

class ProfileViewModel(
    private val logoutUseCase: LogoutUseCase,
) : StatefulViewModel<ProfileAction, ProfileState>(ProfileState()) {

    override suspend fun handleAction(action: ProfileAction) {
        when (action) {
            LogoutAction -> logout()
        }
    }

    private suspend fun logout() {
        if (state.isLoggingOut) return
        logoutUseCase(Unit).fold(
            onLoading = { updateState { it.copy(isLoggingOut = true) } },
            onSuccess = { updateState { it.copy(isLoggingOut = false) } },
            onError = { updateState { it.copy(isLoggingOut = false) } },
        )
    }
}
