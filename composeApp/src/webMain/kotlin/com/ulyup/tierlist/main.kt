package com.ulyup.tierlist

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ulyup.tierlist.core.di.coreModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(coreModule)
    }
    ComposeViewport {
        App()
    }
}