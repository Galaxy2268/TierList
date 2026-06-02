package com.ulyup.tier_list.domain.preferences.usecase

import com.ulyup.tier_list.core.usecase.UseCase
import com.ulyup.tier_list.domain.language.AppLanguage
import com.ulyup.tier_list.domain.preferences.LanguagePreferences

class SaveLanguageUseCase(
    private val languagePreferences: LanguagePreferences,
) : UseCase<AppLanguage, Unit>() {
    override suspend fun execute(parameters: AppLanguage) {
        languagePreferences.save(parameters.localeTag)
    }
}
