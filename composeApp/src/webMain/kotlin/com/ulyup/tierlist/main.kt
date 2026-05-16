package com.ulyup.tierlist

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ulyup.tierlist.core.di.appModules
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(appModules)
    }
    ComposeViewport {
        App()
    }
}