package com.ulyup.tier_list.feature.auth.vm.login

sealed interface LoginAction

value class ChangeUsernameOrEmailAction(val value: String) : LoginAction
value class ChangePasswordAction(val value: String) : LoginAction

data object SubmitAction : LoginAction
