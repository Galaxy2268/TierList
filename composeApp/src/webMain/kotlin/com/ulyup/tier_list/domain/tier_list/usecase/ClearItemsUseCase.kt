package com.ulyup.tier_list.domain.tier_list.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository

class ClearItemsUseCase(
    private val itemRepository: ItemRepository,
) : UseCase<Int, Unit>() {

    override suspend fun execute(parameters: Int) {
        itemRepository.clear(parameters)
    }
}
