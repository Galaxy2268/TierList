package com.ulyup.tierlist.domain.auth.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.auth.repository.AuthRepository

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