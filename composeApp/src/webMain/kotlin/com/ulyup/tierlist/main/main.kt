package com.ulyup.tierlist.main

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ulyup.tierlist.core.di.coreModule
import com.ulyup.tierlist.core.di.authModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(coreModule, authModule)
    }
    ComposeViewport {
        App()
    }
}