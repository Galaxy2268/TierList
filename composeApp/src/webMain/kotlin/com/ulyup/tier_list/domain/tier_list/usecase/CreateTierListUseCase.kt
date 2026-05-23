package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class CreateTierListUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<CreateTierListUseCase.Params, TierList>() {

    override suspend fun execute(parameters: Params): TierList =
        tierListRepository.create(
            title = parameters.title,
            isPublic = parameters.isPublic,
        )

    data class Params(
        val title: String,
        val isPublic: Boolean,
    )
}
