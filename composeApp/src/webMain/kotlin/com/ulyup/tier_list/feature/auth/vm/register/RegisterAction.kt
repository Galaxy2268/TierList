package com.ulyup.tier_list.feature.auth.vm.register

sealed interface RegisterAction

value class ChangeUsernameAction(val value: String) : RegisterAction
value class ChangeEmailAction(val value: String) : RegisterAction
value class ChangePasswordAction(val value: String) : RegisterAction

data object SubmitAction : RegisterAction
