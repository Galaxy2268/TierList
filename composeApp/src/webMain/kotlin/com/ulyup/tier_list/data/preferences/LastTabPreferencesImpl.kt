package com.ulyup.tier_list.data.preferences

import com.ulyup.tier_list.domain.preferences.LastTabPreferences
import kotlinx.browser.window

class LastTabPreferencesImpl : LastTabPreferences {

    override fun save(graphName: String) {
        runCatching { window.localStorage.setItem(KEY, graphName) }
    }

    override fun load(): String? =
        runCatching { window.localStorage.getItem(KEY) }.getOrNull()

    override fun clear() {
        runCatching { window.localStorage.removeItem(KEY) }
    }

    private companion object {
        const val KEY = "tierrank.lastTab"
    }
}
