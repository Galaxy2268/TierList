package com.ulyup.tier_list.feature.profile.vm

sealed interface ProfileEvent

data class ShowErrorMessageEvent(val text: String?) : ProfileEvent
