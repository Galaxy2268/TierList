package com.ulyup.tier_list.domain.language

enum class AppLanguage(val localeTag: String) {
    ENGLISH("en"),
    LATVIAN("lv");

    companion object {
        fun fromTag(tag: String): AppLanguage =
            entries.firstOrNull { tag.lowercase().startsWith(it.localeTag) } ?: ENGLISH
    }
}
