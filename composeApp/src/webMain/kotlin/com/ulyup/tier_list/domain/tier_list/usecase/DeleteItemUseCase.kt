package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository

class DeleteItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<DeleteItemUseCase.Params, Unit>() {

    override suspend fun execute(parameters: Params) {
        itemRepository.delete(
            tierListId = parameters.tierListId,
            itemId = parameters.itemId,
        )
    }

    data class Params(
        val tierListId: Int,
        val itemId: Int,
    )
}
