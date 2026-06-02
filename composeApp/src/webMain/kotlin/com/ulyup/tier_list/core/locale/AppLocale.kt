package com.ulyup.tier_list.core.locale

@OptIn(ExperimentalWasmJsInterop::class)
fun applyAppLanguage(localeTag: String): Unit =
    js("window.__customLocale = localeTag")
