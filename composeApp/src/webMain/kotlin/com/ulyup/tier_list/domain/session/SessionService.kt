package com.ulyup.tier_list.domain.session

import com.ulyup.tier_list.domain.auth.model.User
import kotlinx.coroutines.flow.StateFlow

interface SessionService {
    val sessionState: StateFlow<SessionState>
    val currentUser: StateFlow<User?>
    suspend fun bootstrap()
}
