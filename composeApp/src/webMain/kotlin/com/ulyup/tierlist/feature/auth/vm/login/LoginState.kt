package com.ulyup.tierlist.feature.auth.vm.login

import com.ulyup.tierlist.core.mvi.LoadableState

data class LoginState(
    val usernameOrEmail: String = "",
    val password: String = "",
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<LoginState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}