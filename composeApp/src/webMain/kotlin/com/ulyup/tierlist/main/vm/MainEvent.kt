package com.ulyup.tierlist.main.vm

sealed interface MainEvent {
    data object NavigateToFeed : MainEvent
    data object NavigateToAuth : MainEvent
}
