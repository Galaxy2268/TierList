package com.ulyup.tierlist.core.network.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {

    private val _authState = MutableStateFlow(SessionState.Unknown)
    val authState: StateFlow<SessionState> = _authState.asStateFlow()

    fun authorize() {
        _authState.value = SessionState.Authorized
    }

    fun unauthorize() {
        _authState.value = SessionState.Unauthorized
    }
}