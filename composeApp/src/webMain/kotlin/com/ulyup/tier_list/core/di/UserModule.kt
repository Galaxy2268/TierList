package com.ulyup.tier_list.core.di

import com.ulyup.tier_list.data.user.api.UserApi
import com.ulyup.tier_list.data.user.api.UserApiImpl
import com.ulyup.tier_list.data.user.repository.UserRepositoryImpl
import com.ulyup.tier_list.domain.user.repository.UserRepository
import com.ulyup.tier_list.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tier_list.domain.user.usecase.UpgradePremiumUseCase
import com.ulyup.tier_list.feature.profile.vm.ProfileViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val userModule = module {
    singleOf(::UserApiImpl) bind UserApi::class
    singleOf(::UserRepositoryImpl) bind UserRepository::class
    factoryOf(::ObserveCurrentUserUseCase)
    factoryOf(::UpgradePremiumUseCase)
    viewModelOf(::ProfileViewModel)
}
