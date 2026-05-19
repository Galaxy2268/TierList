package com.ulyup.tierlist.domain.auth.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.auth.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        authRepository.logout()
    }
}
