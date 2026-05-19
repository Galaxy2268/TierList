package com.ulyup.tierlist.feature.auth.vm.login

data class LoginState(
    val usernameOrEmail: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)