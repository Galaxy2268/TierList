package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.core.network.HttpClientFactory
import com.ulyup.tierlist.core.network.session.SessionBootstrapper
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.main.vm.MainViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModelOf
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
    single { SessionBootstrapper(httpClient = get(), sessionManager = get()) }
    viewModelOf(::MainViewModel)
}
