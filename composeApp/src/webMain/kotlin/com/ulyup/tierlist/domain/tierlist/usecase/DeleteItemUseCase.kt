package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.repository.ItemRepository

class DeleteItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<DeleteItemUseCase.Params, Unit>() {

    override suspend fun execute(parameters: Params) {
        itemRepository.delete(
            tierlistId = parameters.tierlistId,
            itemId = parameters.itemId,
        )
    }

    data class Params(
        val tierlistId: Int,
        val itemId: Int,
    )
}
