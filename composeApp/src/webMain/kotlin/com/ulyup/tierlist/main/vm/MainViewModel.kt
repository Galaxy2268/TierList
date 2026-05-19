package com.ulyup.tierlist.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.domain.session.SessionService
import com.ulyup.tierlist.domain.session.SessionState.Authorized
import com.ulyup.tierlist.domain.session.SessionState.Unauthorized
import com.ulyup.tierlist.domain.session.SessionState.Unknown
import com.ulyup.tierlist.feature.auth.navigation.AuthGraph
import com.ulyup.tierlist.feature.feed.navigation.FeedGraph
import com.ulyup.tierlist.feature.splash.navigation.SplashGraph
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionService: SessionService,
) : StatefulViewModel<Unit, MainState>(MainState()) {

    init {
        viewModelScope.launch {
            sessionService.bootstrap()
        }
        viewModelScope.launch {
            sessionService.sessionState.collect { sessionState ->
                val startDestination: Any = when (sessionState) {
                    Unknown -> SplashGraph
                    Authorized -> FeedGraph
                    Unauthorized -> AuthGraph
                }
                updateState { it.copy(startDestination = startDestination) }
            }
        }
    }
}
