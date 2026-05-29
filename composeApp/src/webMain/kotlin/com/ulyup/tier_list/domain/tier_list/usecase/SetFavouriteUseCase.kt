package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository

class SetFavouriteUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<SetFavouriteUseCase.Params, Unit>() {

    override suspend fun execute(parameters: Params) {
        tierListRepository.setFavourite(parameters.tierListId, parameters.favourite)
    }

    data class Params(
        val tierListId: Int,
        val favourite: Boolean,
    )
}
