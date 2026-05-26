package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.preferences.LastTabPreferences

class ClearLastTabUseCase(
    private val lastTabPreferences: LastTabPreferences,
) : UseCase<Unit, Unit>() {
    override suspend fun execute(parameters: Unit) {
        lastTabPreferences.clear()
    }
}
