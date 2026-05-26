package com.ulyup.tier_list.feature.auth.vm.register

import com.ulyup.tier_list.core.mvi.LoadableState

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<RegisterState> {
    val isSubmitEnabled: Boolean
        get() = username.isNotBlank() && email.isNotBlank() && password.isNotBlank()

    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}
