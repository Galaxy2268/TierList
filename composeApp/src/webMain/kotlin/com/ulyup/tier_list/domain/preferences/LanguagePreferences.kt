package com.ulyup.tier_list.domain.preferences

interface LanguagePreferences {
    fun save(localeTag: String)
    fun load(): String?
    fun systemDefault(): String
}
