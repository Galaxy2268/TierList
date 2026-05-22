package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.model.Item
import com.ulyup.tierlist.domain.tierlist.repository.ItemRepository

class CreateItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<CreateItemUseCase.Params, Item>() {

    override suspend fun execute(parameters: Params): Item =
        itemRepository.create(
            tierlistId = parameters.tierlistId,
            bytes = parameters.bytes,
            filename = parameters.filename,
        )

    data class Params(
        val tierlistId: Int,
        val bytes: ByteArray,
        val filename: String,
    )
}
