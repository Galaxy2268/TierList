package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierListDetail
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class GetTierListDetailUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<Int, TierListDetail>() {
    override suspend fun execute(parameters: Int): TierListDetail =
        tierListRepository.getDetail(parameters)
}
