package com.ulyup.tier_list.core.browser

import kotlinx.browser.window

@OptIn(ExperimentalWasmJsInterop::class)
fun reloadApp() {
    window.location.reload()
}
