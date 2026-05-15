package com.ulyup.tierlist.core.mvi

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class StatefulViewModel<I : Any, S : Any>(
    initialState: S,
) : ViewModel() {

    private val mutableStateFlow = MutableStateFlow(initialState)
    val stateFlow: StateFlow<S> = mutableStateFlow.asStateFlow()

    protected val state: S
        get() = mutableStateFlow.value

    protected fun updateState(reducer: (S) -> S) {
        mutableStateFlow.update(reducer)
    }

    val uiState: S
        @Composable
        get() = stateFlow.collectAsStateWithLifecycle().value

    fun onIntent(intent: I) {
        viewModelScope.launch { handleIntent(intent) }
    }

    protected abstract suspend fun handleIntent(intent: I)
}