package com.ulyup.tier_list.data.preferences

import com.ulyup.tier_list.domain.preferences.LanguagePreferences
import kotlinx.browser.window

class LanguagePreferencesImpl : LanguagePreferences {

    override fun save(localeTag: String) {
        runCatching { window.localStorage.setItem(KEY, localeTag) }
    }

    override fun load(): String? =
        runCatching { window.localStorage.getItem(KEY) }.getOrNull()

    @OptIn(ExperimentalWasmJsInterop::class)
    override fun systemDefault(): String =
        runCatching { window.navigator.language }.getOrDefault("")

    private companion object {
        const val KEY = "tierrank.language"
    }
}
