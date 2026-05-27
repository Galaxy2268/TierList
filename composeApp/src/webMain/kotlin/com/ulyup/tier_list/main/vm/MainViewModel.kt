package com.ulyup.tier_list.main.vm

import androidx.lifecycle.viewModelScope
import com.ulyup.tier_list.core.browser.ShareDetailLink
import com.ulyup.tier_list.core.mvi.StatefulViewModel
import com.ulyup.tier_list.core.usecase.fold
import com.ulyup.tier_list.domain.preferences.usecase.ClearLastDetailUseCase
import com.ulyup.tier_list.domain.preferences.usecase.ClearLastTabUseCase
import com.ulyup.tier_list.domain.preferences.usecase.GetLastDetailUseCase
import com.ulyup.tier_list.domain.preferences.usecase.GetLastTabUseCase
import com.ulyup.tier_list.domain.preferences.usecase.SaveLastDetailUseCase
import com.ulyup.tier_list.domain.preferences.usecase.SaveLastTabUseCase
import com.ulyup.tier_list.domain.session.SessionState.Authorized
import com.ulyup.tier_list.domain.session.SessionState.Error
import com.ulyup.tier_list.domain.session.SessionState.Unauthorized
import com.ulyup.tier_list.domain.session.SessionState.Unknown
import com.ulyup.tier_list.domain.session.usecase.BootstrapSessionUseCase
import com.ulyup.tier_list.domain.session.usecase.ObserveSessionStateUseCase
import com.ulyup.tier_list.feature.auth.navigation.AuthGraph
import com.ulyup.tier_list.feature.error.navigation.ErrorGraph
import com.ulyup.tier_list.feature.feed.navigation.FeedGraph
import com.ulyup.tier_list.feature.mylists.navigation.MyListsGraph
import com.ulyup.tier_list.feature.profile.navigation.ProfileGraph
import com.ulyup.tier_list.feature.splash.navigation.SplashGraph
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val bootstrapSessionUseCase: BootstrapSessionUseCase,
    private val observeSessionStateUseCase: ObserveSessionStateUseCase,
    private val getLastTabUseCase: GetLastTabUseCase,
    private val saveLastTabUseCase: SaveLastTabUseCase,
    private val clearLastTabUseCase: ClearLastTabUseCase,
    private val getLastDetailUseCase: GetLastDetailUseCase,
    private val saveLastDetailUseCase: SaveLastDetailUseCase,
    private val clearLastDetailUseCase: ClearLastDetailUseCase,
) : StatefulViewModel<MainAction, MainState>(MainState()) {

    private var bootstrapJob: Job? = null

    init {
        launchBootstrap()
        viewModelScope.launch {
            observeSessionStateUseCase(Unit).collect { sessionState ->
                if (sessionState == Unauthorized) {
                    clearLastTabUseCase(Unit).launchIn(viewModelScope)
                    clearLastDetailUseCase(Unit).launchIn(viewModelScope)
                }
                val startDestination: Any = when (sessionState) {
                    Unknown -> SplashGraph
                    Authorized -> resolveAuthorizedStart()
                    Unauthorized -> AuthGraph
                    Error -> ErrorGraph
                }
                val initialDetailId = if (sessionState == Authorized) resolveInitialDetailId() else null
                updateState {
                    it.copy(
                        startDestination = startDestination,
                        initialDetailId = initialDetailId,
                    )
                }
            }
        }
    }

    override suspend fun handleAction(action: MainAction) {
        when (action) {
            RetryBootstrapAction -> launchBootstrap()
            is SaveLastTabAction -> saveLastTabUseCase(action.graphName).launchIn(viewModelScope)
            is SaveLastDetailAction -> saveLastDetailUseCase(action.tierListId).launchIn(viewModelScope)
            ClearLastDetailAction -> clearLastDetailUseCase(Unit).launchIn(viewModelScope)
        }
    }

    private suspend fun resolveAuthorizedStart(): Any {
        var graph: Any = FeedGraph
        getLastTabUseCase(Unit).fold(
            onSuccess = { name -> graph = TAB_GRAPHS[name] ?: FeedGraph },
        )
        return graph
    }

    private suspend fun resolveInitialDetailId(): Int? {
        val urlId = ShareDetailLink.parseFromUrl()
        if (urlId != null) return urlId
        var detailId: Int? = null
        getLastDetailUseCase(Unit).fold(
            onSuccess = { id -> detailId = id },
        )
        return detailId
    }

    private fun launchBootstrap() {
        bootstrapJob?.cancel()
        bootstrapJob = bootstrapSessionUseCase(Unit).launchIn(viewModelScope)
    }

    private companion object {
        val TAB_GRAPHS: Map<String, Any> = mapOf(
            FeedGraph.toString() to FeedGraph,
            MyListsGraph.toString() to MyListsGraph,
            ProfileGraph.toString() to ProfileGraph,
        )
    }
}
