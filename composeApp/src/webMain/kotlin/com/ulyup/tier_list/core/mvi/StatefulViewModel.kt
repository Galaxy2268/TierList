package com.ulyup.tier_list.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class StatefulViewModel<A : Any, S : Any>(
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
        get() = stateFlow.collectAsState().value

    fun onAction(action: A) {
        viewModelScope.launch { handleAction(action) }
    }

    protected abstract suspend fun handleAction(action: A)
}