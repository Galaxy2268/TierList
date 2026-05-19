package com.ulyup.tierlist.domain.user.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.auth.model.User
import com.ulyup.tierlist.domain.user.repository.UserRepository

class UpgradePremiumUseCase(
    private val userRepository: UserRepository,
) : UseCase<Unit, User>() {
    override suspend fun execute(parameters: Unit): User =
        userRepository.upgradePremium()
}
