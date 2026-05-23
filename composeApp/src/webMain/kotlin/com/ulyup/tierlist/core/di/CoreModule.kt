package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.data.network.HttpClientFactory
import com.ulyup.tierlist.data.session.SessionManager
import com.ulyup.tierlist.data.session.SessionServiceImpl
import com.ulyup.tierlist.domain.session.SessionService
import com.ulyup.tierlist.domain.session.usecase.BootstrapSessionUseCase
import com.ulyup.tierlist.domain.session.usecase.ObserveSessionStateUseCase
import com.ulyup.tierlist.main.vm.MainViewModel
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
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
    singleOf(::SessionServiceImpl) bind SessionService::class
    factoryOf(::BootstrapSessionUseCase)
    factoryOf(::ObserveSessionStateUseCase)
    viewModelOf(::MainViewModel)
}
