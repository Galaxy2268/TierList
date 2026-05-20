package com.ulyup.tierlist.domain.tierlist.usecase

import com.ulyup.tierlist.core.usecase.UseCase
import com.ulyup.tierlist.domain.tierlist.model.TierlistDetail
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository

class GetTierlistDetailUseCase(
    private val tierlistRepository: TierlistRepository,
) : UseCase<Int, TierlistDetail>() {
    override suspend fun execute(parameters: Int): TierlistDetail =
        tierlistRepository.getDetail(parameters)
}
