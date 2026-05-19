package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.data.auth.api.AuthApi
import com.ulyup.tierlist.data.auth.api.AuthApiImpl
import com.ulyup.tierlist.data.auth.repository.AuthRepositoryImpl
import com.ulyup.tierlist.domain.auth.repository.AuthRepository
import com.ulyup.tierlist.domain.auth.usecase.LoginUseCase
import com.ulyup.tierlist.domain.auth.usecase.RegisterUseCase
import com.ulyup.tierlist.feature.auth.vm.login.LoginViewModel
import com.ulyup.tierlist.feature.auth.vm.register.RegisterViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authModule = module {
    singleOf(::AuthApiImpl) bind AuthApi::class
    singleOf(::AuthRepositoryImpl) bind AuthRepository::class
    factoryOf(::LoginUseCase)
    factoryOf(::RegisterUseCase)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}