package com.ulyup.tier_list.feature.profile.vm

sealed interface ProfileAction

data object ShowLogoutConfirmAction : ProfileAction
data object DismissLogoutConfirmAction : ProfileAction
data object ConfirmLogoutAction : ProfileAction
data object UpgradePremiumAction : ProfileAction
