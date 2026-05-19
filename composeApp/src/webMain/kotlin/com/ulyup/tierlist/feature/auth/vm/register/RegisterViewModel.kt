package com.ulyup.tierlist.feature.auth.vm.register

import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.auth.usecase.RegisterUseCase

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : StatefulViewModel<RegisterAction, RegisterState>(RegisterState()) {

    override suspend fun handleAction(action: RegisterAction) {
        when (action) {
            is ChangeUsernameAction ->
                updateState { it.copy(username = action.value, errorMessage = null) }
            is ChangeEmailAction ->
                updateState { it.copy(email = action.value, errorMessage = null) }
            is ChangePasswordAction ->
                updateState { it.copy(password = action.value, errorMessage = null) }
            SubmitAction -> submit()
        }
    }

    private suspend fun submit() {
        val current = state
        if (current.isLoading) return
        registerUseCase(
            RegisterUseCase.Params(
                username = current.username,
                email = current.email,
                password = current.password,
            )
        ).fold(
            onLoading = { updateState { it.copy(isLoading = true, errorMessage = null) } },
            onSuccess = { updateState { it.copy(isLoading = false) } },
            onError = { exception ->
                updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message,
                    )
                }
            },
        )
    }
}