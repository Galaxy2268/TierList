package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.model.Tierlist
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository

class GetMyTierlistsUseCase(
    private val tierlistRepository: TierlistRepository,
) : UseCase<Unit, List<Tierlist>>() {
    override suspend fun execute(parameters: Unit): List<Tierlist> =
        tierlistRepository.getUserTierlists()
}
