package com.ulyup.tier_list.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.domain.session.SessionState.Authorized
import com.ulyup.tier_list.domain.session.SessionState.Error
import com.ulyup.tier_list.domain.session.SessionState.Unauthorized
import com.ulyup.tier_list.domain.session.SessionState.Unknown
import com.ulyup.tier_list.domain.session.usecase.BootstrapSessionUseCase
import com.ulyup.tier_list.domain.session.usecase.ObserveSessionStateUseCase
import com.ulyup.tier_list.feature.auth.navigation.AuthGraph
import com.ulyup.tier_list.feature.error.navigation.ErrorGraph
import com.ulyup.tier_list.feature.feed.navigation.FeedGraph
import com.ulyup.tier_list.feature.splash.navigation.SplashGraph
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
