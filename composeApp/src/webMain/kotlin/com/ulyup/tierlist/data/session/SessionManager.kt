package com.ulyup.tierlist.data.session

import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.session.SessionState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager {

    private val _authState = MutableStateFlow(SessionState.Unknown)
    val authState: StateFlow<SessionState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun unknown() {
        _authState.value = SessionState.Unknown
    }

    fun signIn(user: User) {
        _currentUser.value = user
        _authState.value = SessionState.Authorized
    }

    fun signOut() {
        _currentUser.value = null
        _authState.value = SessionState.Unauthorized
    }

    fun unauthorize() = signOut()

    fun error() {
        _authState.value = SessionState.Error
    }

    fun setCurrentUser(user: User) {
        if (_authState.value != SessionState.Authorized) return
        _currentUser.value = user
    }
}
