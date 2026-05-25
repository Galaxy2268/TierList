package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class SetTierListVisibilityUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<SetTierListVisibilityUseCase.Params, TierList>() {

    override suspend fun execute(parameters: Params): TierList =
        tierListRepository.setVisibility(id = parameters.id, isPublic = parameters.isPublic)

    data class Params(
        val id: Int,
        val isPublic: Boolean,
    )
}
