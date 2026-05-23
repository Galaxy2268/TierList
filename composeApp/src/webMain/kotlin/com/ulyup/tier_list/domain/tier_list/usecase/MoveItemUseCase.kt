package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository
import com.ulyup.tier_list.model.Tier

class MoveItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<MoveItemUseCase.Params, TierListItem>() {

    override suspend fun execute(parameters: Params): TierListItem =
        itemRepository.move(
            tierListId = parameters.tierListId,
            itemId = parameters.itemId,
            tier = parameters.tier,
            position = parameters.position,
        )

    data class Params(
        val tierListId: Int,
        val itemId: Int,
        val tier: Tier?,
        val position: Int,
    )
}
