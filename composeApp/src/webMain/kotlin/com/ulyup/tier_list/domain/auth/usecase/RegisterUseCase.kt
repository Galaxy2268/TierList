package com.ulyup.tier_list.domain.auth.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.auth.repository.AuthRepository

class RegisterUseCase(
    private val authRepository: AuthRepository,
) : UseCase<RegisterUseCase.Params, User>() {

    override suspend fun execute(parameters: Params): User =
        authRepository.register(
            username = parameters.username,
            email = parameters.email,
            password = parameters.password,
        )

    data class Params(
        val username: String,
        val email: String,
        val password: String,
    )
}