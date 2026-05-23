package com.ulyup.tier_list.domain.auth.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.auth.repository.AuthRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        authRepository.logout()
    }
}
