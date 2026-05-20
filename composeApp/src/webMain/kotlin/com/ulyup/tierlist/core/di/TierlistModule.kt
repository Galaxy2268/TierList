package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.data.tierlist.api.TierlistApi
import com.ulyup.tierlist.data.tierlist.api.TierlistApiImpl
import com.ulyup.tierlist.data.tierlist.repository.TierlistRepositoryImpl
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository
import com.ulyup.tierlist.domain.tierlist.usecase.CreateTierlistUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetMyTierlistsUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetPublicTierlistsUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetTierlistDetailUseCase
import com.ulyup.tierlist.feature.feed.vm.FeedViewModel
import com.ulyup.tierlist.feature.mylists.vm.MyListsViewModel
import com.ulyup.tierlist.feature.tierlist.detail.vm.TierlistDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tierlistModule = module {
    singleOf(::TierlistApiImpl) bind TierlistApi::class
    singleOf(::TierlistRepositoryImpl) bind TierlistRepository::class
    factoryOf(::GetPublicTierlistsUseCase)
    factoryOf(::GetMyTierlistsUseCase)
    factoryOf(::CreateTierlistUseCase)
    factoryOf(::GetTierlistDetailUseCase)
    viewModelOf(::FeedViewModel)
    viewModelOf(::MyListsViewModel)
    viewModelOf(::TierlistDetailViewModel)
}
