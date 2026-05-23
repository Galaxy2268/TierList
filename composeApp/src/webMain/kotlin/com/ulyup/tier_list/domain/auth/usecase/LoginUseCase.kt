package com.ulyup.tier_list.domain.auth.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.auth.repository.AuthRepository

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