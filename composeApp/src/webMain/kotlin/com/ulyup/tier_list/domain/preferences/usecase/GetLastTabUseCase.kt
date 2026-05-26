package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.preferences.LastTabPreferences

class GetLastTabUseCase(
    private val lastTabPreferences: LastTabPreferences,
) : UseCase<Unit, String?>() {
    override suspend fun execute(parameters: Unit): String? = lastTabPreferences.load()
}
