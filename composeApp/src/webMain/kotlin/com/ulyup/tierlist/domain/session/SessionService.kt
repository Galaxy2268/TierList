package com.ulyup.tierlist.domain.session

import com.ulyup.tierlist.domain.auth.model.User
import kotlinx.coroutines.flow.StateFlow

interface SessionService {
    val sessionState: StateFlow<SessionState>
    val currentUser: StateFlow<User?>
    suspend fun bootstrap()
}
