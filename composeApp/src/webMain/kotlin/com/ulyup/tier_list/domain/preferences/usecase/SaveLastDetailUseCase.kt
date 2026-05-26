package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.preferences.LastDetailPreferences

class SaveLastDetailUseCase(
    private val lastDetailPreferences: LastDetailPreferences,
) : UseCase<Int, Unit>() {
    override suspend fun execute(parameters: Int) {
        lastDetailPreferences.save(parameters)
    }
}
