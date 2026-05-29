package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierList
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository
import com.ulyup.tier_list.domain.tier_list.util.favouritesFirst

class GetPublicTierListsUseCase(
    private val tierListRepository: TierListRepository,
) : UseCase<Unit, List<TierList>>() {
    override suspend fun execute(parameters: Unit): List<TierList> =
        tierListRepository.getPublicTierLists().favouritesFirst()
}
