package com.ulyup.tierlist.domain.session.usecase

import com.ulyup.tierlist.core.usecase.ObserveUseCase
import com.ulyup.tierlist.domain.session.SessionService
import com.ulyup.tierlist.domain.session.SessionState
import kotlinx.coroutines.flow.Flow

class ObserveSessionStateUseCase(
    private val sessionService: SessionService,
) : ObserveUseCase<Unit, SessionState>() {
    override fun execute(parameters: Unit): Flow<SessionState> =
        sessionService.sessionState
}
