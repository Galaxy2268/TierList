package com.ulyup.tier_list.data.preferences

import com.ulyup.tier_list.domain.preferences.LastDetailPreferences
import kotlinx.browser.window

class LastDetailPreferencesImpl : LastDetailPreferences {

    override fun save(tierListId: Int) {
        runCatching { window.localStorage.setItem(KEY, tierListId.toString()) }
    }

    override fun load(): Int? =
        runCatching { window.localStorage.getItem(KEY) }.getOrNull()?.toIntOrNull()

    override fun clear() {
        runCatching { window.localStorage.removeItem(KEY) }
    }

    private companion object {
        const val KEY = "tierrank.lastDetail"
    }
}
