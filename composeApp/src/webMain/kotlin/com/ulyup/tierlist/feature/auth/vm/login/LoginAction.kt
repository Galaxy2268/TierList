package com.ulyup.tierlist.feature.auth.vm.login

sealed interface LoginAction

data class ChangeUsernameOrEmailAction(val value: String) : LoginAction
data class ChangePasswordAction(val value: String) : LoginAction
data object SubmitAction : LoginAction