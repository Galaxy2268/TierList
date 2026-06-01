package com.ulyup.tier_list.core.browser

import kotlinx.browser.window

object ShareDetailLink {

    private const val QUERY_KEY = "detail"

    @OptIn(ExperimentalWasmJsInterop::class)
    fun shareUrlFor(tierListId: Int): String =
        "${window.location.origin}${window.location.pathname}?$QUERY_KEY=$tierListId"

    fun parseFromUrl(): Int? =
        window.location.search
            .removePrefix("?")
            .split("&")
            .firstOrNull { it.startsWith("$QUERY_KEY=") }
            ?.removePrefix("$QUERY_KEY=")
            ?.toIntOrNull()

    @OptIn(ExperimentalWasmJsInterop::class)
    fun setInUrl(tierListId: Int) {
        window.history.replaceState(null, "", "?$QUERY_KEY=$tierListId")
    }

    @OptIn(ExperimentalWasmJsInterop::class)
    fun clearFromUrl() {
        window.history.replaceState(null, "", window.location.pathname)
    }
}
