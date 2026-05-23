package com.ulyup.tierlist.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tierlist.core.mvi.StatefulViewModel
import com.ulyup.tierlist.domain.session.SessionState.Authorized
import com.ulyup.tierlist.domain.session.SessionState.Error
import com.ulyup.tierlist.domain.session.SessionState.Unauthorized
import com.ulyup.tierlist.domain.session.SessionState.Unknown
import com.ulyup.tierlist.domain.session.usecase.BootstrapSessionUseCase
import com.ulyup.tierlist.domain.session.usecase.ObserveSessionStateUseCase
import com.ulyup.tierlist.feature.auth.navigation.AuthGraph
import com.ulyup.tierlist.feature.error.navigation.ErrorGraph
import com.ulyup.tierlist.feature.feed.navigation.FeedGraph
import com.ulyup.tierlist.feature.splash.navigation.SplashGraph
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val bootstrapSessionUseCase: BootstrapSessionUseCase,
    private val observeSessionStateUseCase: ObserveSessionStateUseCase,
) : StatefulViewModel<MainAction, MainState>(MainState()) {

    private var bootstrapJob: Job? = null

    init {
        launchBootstrap()
        viewModelScope.launch {
            observeSessionStateUseCase(Unit).collect { sessionState ->
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
            RetryBootstrapAction -> launchBootstrap()
        }
    }

    private fun launchBootstrap() {
        bootstrapJob?.cancel()
        bootstrapJob = bootstrapSessionUseCase(Unit).launchIn(viewModelScope)
    }
}
