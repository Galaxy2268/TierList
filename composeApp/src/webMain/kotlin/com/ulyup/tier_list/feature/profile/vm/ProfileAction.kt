package com.ulyup.tier_list.feature.profile.vm

sealed interface ProfileAction

data object LogoutAction : ProfileAction
