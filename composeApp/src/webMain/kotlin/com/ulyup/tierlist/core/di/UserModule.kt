package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.data.user.api.UserApi
import com.ulyup.tierlist.data.user.api.UserApiImpl
import com.ulyup.tierlist.data.user.repository.UserRepositoryImpl
import com.ulyup.tierlist.domain.user.repository.UserRepository
import com.ulyup.tierlist.domain.user.usecase.ObserveCurrentUserUseCase
import com.ulyup.tierlist.domain.user.usecase.UpgradePremiumUseCase
import com.ulyup.tierlist.feature.profile.vm.ProfileViewModel
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
