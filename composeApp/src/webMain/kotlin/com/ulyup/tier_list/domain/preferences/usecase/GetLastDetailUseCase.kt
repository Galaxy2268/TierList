package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.preferences.LastDetailPreferences

class GetLastDetailUseCase(
    private val lastDetailPreferences: LastDetailPreferences,
) : UseCase<Unit, Int?>() {
    override suspend fun execute(parameters: Unit): Int? = lastDetailPreferences.load()
}
