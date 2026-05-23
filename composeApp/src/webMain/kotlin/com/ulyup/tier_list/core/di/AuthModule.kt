package com.ulyup.tier_list.core.di

import com.ulyup.tier_list.data.auth.api.AuthApi
import com.ulyup.tier_list.data.auth.api.AuthApiImpl
import com.ulyup.tier_list.data.auth.repository.AuthRepositoryImpl
import com.ulyup.tier_list.domain.auth.repository.AuthRepository
import com.ulyup.tier_list.domain.auth.usecase.LoginUseCase
import com.ulyup.tier_list.domain.auth.usecase.LogoutUseCase
import com.ulyup.tier_list.domain.auth.usecase.RegisterUseCase
import com.ulyup.tier_list.feature.auth.vm.login.LoginViewModel
import com.ulyup.tier_list.feature.auth.vm.register.RegisterViewModel
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
    factoryOf(::LogoutUseCase)
    viewModelOf(::LoginViewModel)
    viewModelOf(::RegisterViewModel)
}