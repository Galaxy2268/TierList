package com.ulyup.tierlist.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.InteractiveStatefulViewModel
import com.ulyup.tierlist.core.network.session.SessionBootstrapper
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.session.SessionState
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionManager: SessionManager,
    private val sessionBootstrapper: SessionBootstrapper,
) : InteractiveStatefulViewModel<Unit, MainState, MainEvent>(MainState()) {

    init {
        viewModelScope.launch {
            sessionBootstrapper.bootstrap()
        }
        viewModelScope.launch {
            sessionManager.authState.collect { sessionState ->
                when (sessionState) {
                    SessionState.Unknown -> Unit
                    SessionState.Authorized -> {
                        updateState { it.copy(isReady = true) }
                        sendEffect(MainEvent.NavigateToFeed)
                    }
                    SessionState.Unauthorized -> {
                        updateState { it.copy(isReady = true) }
                        sendEffect(MainEvent.NavigateToAuth)
                    }
                }
            }
        }
    }
}
