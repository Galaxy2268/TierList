package com.ulyup.tierlist.feature.auth.vm.register

sealed interface RegisterAction

data class ChangeUsernameAction(val value: String) : RegisterAction
data class ChangeEmailAction(val value: String) : RegisterAction
data class ChangePasswordAction(val value: String) : RegisterAction
data object SubmitAction : RegisterAction