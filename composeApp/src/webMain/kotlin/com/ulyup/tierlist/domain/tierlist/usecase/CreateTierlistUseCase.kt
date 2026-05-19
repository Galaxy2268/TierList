package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository

class CreateTierlistUseCase(
    private val tierlistRepository: TierlistRepository,
) : UseCase<CreateTierlistUseCase.Params, Tierlist>() {

    override suspend fun execute(parameters: Params): Tierlist =
        tierlistRepository.create(
            title = parameters.title,
            isPublic = parameters.isPublic,
        )

    data class Params(
        val title: String,
        val isPublic: Boolean,
    )
}
