package com.ulyup.tier_list.feature.profile.vm

import com.ulyup.tier_list.domain.language.AppLanguage

sealed interface ProfileAction

data object ShowLogoutConfirmAction : ProfileAction
data object DismissLogoutConfirmAction : ProfileAction
data object ConfirmLogoutAction : ProfileAction
data object UpgradePremiumAction : ProfileAction
value class ChangeLanguageAction(val language: AppLanguage) : ProfileAction
