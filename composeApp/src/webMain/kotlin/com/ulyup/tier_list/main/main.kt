package com.ulyup.tier_list.main

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import coil3.ImageLoader
import coil3.SingletonImageLoader
import coil3.network.ktor3.KtorNetworkFetcherFactory
import com.ulyup.tier_list.core.di.authModule
import com.ulyup.tier_list.core.di.coreModule
import com.ulyup.tier_list.core.di.tierListModule
import com.ulyup.tier_list.core.di.userModule
import com.ulyup.tier_list.core.locale.applyAppLanguage
import com.ulyup.tier_list.domain.preferences.LanguagePreferences
import io.ktor.client.HttpClient
import org.koin.core.context.startKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val koinApp = startKoin {
        modules(coreModule, authModule, tierListModule, userModule)
    }
    koinApp.koin.get<LanguagePreferences>().load()?.let { applyAppLanguage(it) }
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