package com.ulyup.tier_list.domain.session.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.session.SessionService

class BootstrapSessionUseCase(
    private val sessionService: SessionService,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        sessionService.bootstrap()
    }
}
