package com.ulyup.tier_list.core.di

import com.ulyup.tier_list.data.network.HttpClientFactory
import com.ulyup.tier_list.data.session.SessionManager
import com.ulyup.tier_list.data.session.SessionServiceImpl
import com.ulyup.tier_list.domain.session.SessionService
import com.ulyup.tier_list.domain.session.usecase.BootstrapSessionUseCase
import com.ulyup.tier_list.domain.session.usecase.ObserveSessionStateUseCase
import com.ulyup.tier_list.main.vm.MainViewModel
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
