package com.ulyup.tierlist.domain.session.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.session.SessionService

class BootstrapSessionUseCase(
    private val sessionService: SessionService,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        sessionService.bootstrap()
    }
}
