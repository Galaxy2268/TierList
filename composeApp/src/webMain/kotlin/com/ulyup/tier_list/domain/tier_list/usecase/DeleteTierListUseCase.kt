package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class DeleteTierListUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<Int, Unit>() {

    override suspend fun execute(parameters: Int) {
        tierListRepository.delete(parameters)
    }
}
