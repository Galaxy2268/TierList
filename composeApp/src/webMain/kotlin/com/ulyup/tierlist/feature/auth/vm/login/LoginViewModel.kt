package com.ulyup.tierlist.feature.auth.vm.login

import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.core.usecase.fold
import com.ulyup.tierlist.domain.auth.usecase.LoginUseCase

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
            onSuccess = { updateState { it.withLoaded() } },
            onError = { exception -> updateState { it.withError(exception.message) } },
        )
    }
}