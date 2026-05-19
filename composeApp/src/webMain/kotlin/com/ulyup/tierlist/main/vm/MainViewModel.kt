package com.ulyup.tierlist.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.domain.session.SessionService
import com.ulyup.tierlist.domain.session.SessionState.Authorized
import com.ulyup.tierlist.domain.session.SessionState.Error
import com.ulyup.tierlist.domain.session.SessionState.Unauthorized
import com.ulyup.tierlist.domain.session.SessionState.Unknown
import com.ulyup.tierlist.feature.auth.navigation.AuthGraph
import com.ulyup.tierlist.feature.error.navigation.ErrorGraph
import com.ulyup.tierlist.feature.feed.navigation.FeedGraph
import com.ulyup.tierlist.feature.splash.navigation.SplashGraph
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionService: SessionService,
) : StatefulViewModel<MainAction, MainState>(MainState()) {

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
                    Error -> ErrorGraph
                }
                updateState { it.copy(startDestination = startDestination) }
            }
        }
    }

    override suspend fun handleAction(action: MainAction) {
        when (action) {
            RetryBootstrapAction -> sessionService.bootstrap()
        }
    }
}