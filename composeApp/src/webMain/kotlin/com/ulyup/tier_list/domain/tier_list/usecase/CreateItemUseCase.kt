package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository

class CreateItemUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<CreateItemUseCase.Params, TierListItem>() {

    override suspend fun execute(parameters: Params): TierListItem =
        itemRepository.create(
            tierListId = parameters.tierListId,
            bytes = parameters.bytes,
            filename = parameters.filename,
        )

    data class Params(
        val tierListId: Int,
        val bytes: ByteArray,
        val filename: String,
    )
}
