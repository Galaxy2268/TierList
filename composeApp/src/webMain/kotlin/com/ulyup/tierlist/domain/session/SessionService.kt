package com.ulyup.tierlist.domain.session

import kotlinx.coroutines.flow.StateFlow

interface SessionService {
    val sessionState: StateFlow<SessionState>
    suspend fun bootstrap()
}