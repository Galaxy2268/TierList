package com.ulyup.tier_list.feature.auth.vm.register

import com.ulyup.tier_list.USERNAME_MAX_LENGTH
import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.auth.usecase.RegisterUseCase

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : StatefulViewModel<RegisterAction, RegisterState>(RegisterState()) {

    override suspend fun handleAction(action: RegisterAction) {
        when (action) {
            is ChangeUsernameAction ->
                updateState { it.copy(username = action.value.take(USERNAME_MAX_LENGTH), errorMessage = null) }
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
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { updateState { it.withSuccess() } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}