package com.ulyup.tierlist.main.vm

sealed interface MainEvent

data object NavigateToFeedEvent : MainEvent
data object NavigateToAuthEvent : MainEvent