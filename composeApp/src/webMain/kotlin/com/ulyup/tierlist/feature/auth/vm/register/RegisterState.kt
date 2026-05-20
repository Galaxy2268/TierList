package com.ulyup.tierlist.feature.auth.vm.register

import com.ulyup.tierlist.core.mvi.LoadableState

data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    override val isLoading: Boolean = false,
    override val errorMessage: String? = null,
) : LoadableState<RegisterState> {
    override fun copyLoadable(isLoading: Boolean, errorMessage: String?) =
        copy(isLoading = isLoading, errorMessage = errorMessage)
}