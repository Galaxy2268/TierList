package com.ulyup.tier_list.domain.session.usecase

import com.ulyup.tier_list.core.usecase.ObserveUseCase
import com.ulyup.tier_list.domain.session.SessionService
import com.ulyup.tier_list.domain.session.SessionState
import kotlinx.coroutines.flow.Flow

class ObserveSessionStateUseCase(
    private val sessionService: SessionService,
) : ObserveUseCase<Unit, SessionState>() {
    override fun execute(parameters: Unit): Flow<SessionState> =
        sessionService.sessionState
}
