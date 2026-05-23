package com.ulyup.tier_list.domain.user.usecase

import com.ulyup.tier_list.core.usecase.ObserveUseCase
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.session.SessionService
import kotlinx.coroutines.flow.Flow

class ObserveCurrentUserUseCase(
    private val sessionService: SessionService,
) : ObserveUseCase<Unit, User?>() {
    override fun execute(parameters: Unit): Flow<User?> =
        sessionService.currentUser
}
