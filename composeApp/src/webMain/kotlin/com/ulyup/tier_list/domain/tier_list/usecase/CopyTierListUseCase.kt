package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class CopyTierListUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<Int, TierList>() {

    override suspend fun execute(parameters: Int): TierList =
        tierListRepository.copy(parameters)
}
