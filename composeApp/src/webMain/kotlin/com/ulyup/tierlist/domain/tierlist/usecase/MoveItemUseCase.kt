package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.domain.tierlist.repository.ItemRepository
import com.ulyup.tierlist.model.Tier

class MoveItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<MoveItemUseCase.Params, Item>() {

    override suspend fun execute(parameters: Params): Item =
        itemRepository.move(
            tierlistId = parameters.tierlistId,
            itemId = parameters.itemId,
            tier = parameters.tier,
            position = parameters.position,
        )

    data class Params(
        val tierlistId: Int,
        val itemId: Int,
        val tier: Tier?,
        val position: Int,
    )
}
