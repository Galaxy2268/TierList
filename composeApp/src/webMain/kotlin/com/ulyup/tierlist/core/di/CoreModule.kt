package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.core.network.HttpClientFactory
import com.ulyup.tierlist.core.network.session.SessionManager
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val coreModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
            encodeDefaults = true
        }
    }
    single { SessionManager() }
    single { HttpClientFactory.create(json = get(), sessionManager = get()) }
}