package com.ulyup.tierlist.domain.user.usecase

import com.ulyup.tierlist.core.usecase.ObserveUseCase
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.session.SessionService
import kotlinx.coroutines.flow.Flow

class ObserveCurrentUserUseCase(
    private val sessionService: SessionService,
) : ObserveUseCase<Unit, User?>() {
    override fun execute(parameters: Unit): Flow<User?> =
        sessionService.currentUser
}
