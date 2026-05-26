package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.preferences.LastTabPreferences

class SaveLastTabUseCase(
    private val lastTabPreferences: LastTabPreferences,
) : UseCase<String, Unit>() {
    override suspend fun execute(parameters: String) {
        lastTabPreferences.save(parameters)
    }
}
