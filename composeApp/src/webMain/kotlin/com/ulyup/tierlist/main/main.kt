package com.ulyup.tierlist.main

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.ulyup.tierlist.core.di.authModule
import com.ulyup.tierlist.core.di.coreModule
import com.ulyup.tierlist.core.di.tierlistModule
import com.ulyup.tierlist.core.di.userModule
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    startKoin {
        modules(coreModule, authModule, tierlistModule, userModule)
    }
    ComposeViewport {
        App()
    }
}