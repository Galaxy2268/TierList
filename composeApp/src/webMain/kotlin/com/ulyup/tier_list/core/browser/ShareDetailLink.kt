package com.ulyup.tier_list.core.browser

import kotlinx.browser.window

object ShareDetailLink {

    private const val QUERY_KEY = "detail"

    @OptIn(ExperimentalWasmJsInterop::class)
    fun buildShareUrl(tierListId: Int): String {
        val pathname = window.location.pathname
        val newPath = "$pathname?$QUERY_KEY=$tierListId"
        return "${window.location.origin}$newPath"
    }

    fun parseFromUrl(): Int? =
        window.location.search
            .removePrefix("?")
            .split("&")
            .firstOrNull { it.startsWith("$QUERY_KEY=") }
            ?.removePrefix("$QUERY_KEY=")
            ?.toIntOrNull()

    @OptIn(ExperimentalWasmJsInterop::class)
    fun clearFromUrl() {
        window.history.replaceState(null, "", window.location.pathname)
    }
}
