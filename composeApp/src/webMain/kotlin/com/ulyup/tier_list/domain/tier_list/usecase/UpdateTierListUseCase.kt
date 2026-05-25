package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class UpdateTierListUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<UpdateTierListUseCase.Params, TierList>() {

    override suspend fun execute(parameters: Params): TierList =
        tierListRepository.update(id = parameters.id, title = parameters.title)

    data class Params(
        val id: Int,
        val title: String,
    )
}
