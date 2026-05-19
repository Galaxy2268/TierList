package com.ulyup.tierlist.feature.profile.vm

sealed interface ProfileAction

data object LogoutAction : ProfileAction
