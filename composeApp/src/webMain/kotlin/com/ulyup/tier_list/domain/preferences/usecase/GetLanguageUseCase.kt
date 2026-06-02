package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.language.AppLanguage
import com.ulyup.tier_list.domain.preferences.LanguagePreferences

class GetLanguageUseCase(
    private val languagePreferences: LanguagePreferences,
) : UseCase<Unit, AppLanguage>() {
    override suspend fun execute(parameters: Unit): AppLanguage =
        AppLanguage.fromTag(languagePreferences.load() ?: languagePreferences.systemDefault())
}
