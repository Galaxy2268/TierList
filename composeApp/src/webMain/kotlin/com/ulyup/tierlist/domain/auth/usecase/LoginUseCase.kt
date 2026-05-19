package com.ulyup.tierlist.domain.auth.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.auth.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository,
) : UseCase<LoginUseCase.Params, User>() {

    override suspend fun execute(parameters: Params): User =
        authRepository.login(
            usernameOrEmail = parameters.usernameOrEmail,
            password = parameters.password,
        )

    data class Params(
        val usernameOrEmail: String,
        val password: String,
    )
}