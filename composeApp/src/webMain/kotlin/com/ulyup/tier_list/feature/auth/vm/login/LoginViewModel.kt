package com.ulyup.tier_list.feature.auth.vm.login

import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.auth.usecase.LoginUseCase

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : StatefulViewModel<LoginAction, LoginState>(LoginState()) {

    override suspend fun handleAction(action: LoginAction) {
        when (action) {
            is ChangeUsernameOrEmailAction ->
                updateState { it.copy(usernameOrEmail = action.value, errorMessage = null) }
            is ChangePasswordAction ->
                updateState { it.copy(password = action.value, errorMessage = null) }
            SubmitAction -> submit()
        }
    }

    private suspend fun submit() {
        val current = state
        if (current.isLoading) return
        loginUseCase(
            LoginUseCase.Params(
                usernameOrEmail = current.usernameOrEmail,
                password = current.password,
            )
        ).fold(
            onLoading = { updateState { it.withLoading() } },
            onSuccess = { updateState { it.withSuccess() } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}