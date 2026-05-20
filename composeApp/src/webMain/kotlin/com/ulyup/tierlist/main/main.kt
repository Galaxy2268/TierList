package com.ulyup.tierlist.main

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.ulyup.tierlist.core.di.authModule
import com.ulyup.tierlist.core.di.coreModule
import com.ulyup.tierlist.core.di.tierlistModule
import com.ulyup.tierlist.core.di.userModule
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val koinApp = startKoin {
        modules(coreModule, authModule, tierlistModule, userModule)
    }
    val httpClient = koinApp.koin.get<HttpClient>()
    SingletonImageLoader.setSafe { context ->
        ImageLoader.Builder(context)
            .components { add(KtorNetworkFetcherFactory(httpClient = httpClient)) }
            .build()
    }
    ComposeViewport {
        App()
    }
}