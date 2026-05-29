package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.model.BatchUploadResult
import com.ulyup.tier_list.domain.tier_list.model.ItemImage
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository

class CreateItemsBatchUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<CreateItemsBatchUseCase.Params, BatchUploadResult>() {

    override suspend fun execute(parameters: Params): BatchUploadResult =
        itemRepository.createBatch(
            tierListId = parameters.tierListId,
            images = parameters.images,
        )

    data class Params(
        val tierListId: Int,
        val images: List<ItemImage>,
    )
}
