package com.ulyup.tier_list.domain.user.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.user.repository.UserRepository

class UpgradePremiumUseCase(
    private val userRepository: UserRepository,
) : UseCase<Unit, User>() {
    override suspend fun execute(parameters: Unit): User =
        userRepository.upgradePremium()
}
