package com.ulyup.tierlist.core.di

import com.ulyup.tierlist.data.tierlist.api.ItemApi
import com.ulyup.tierlist.data.tierlist.api.ItemApiImpl
import com.ulyup.tierlist.data.tierlist.api.TierlistApi
import com.ulyup.tierlist.data.tierlist.api.TierlistApiImpl
import com.ulyup.tierlist.data.tierlist.repository.ItemRepositoryImpl
import com.ulyup.tierlist.data.tierlist.repository.TierlistRepositoryImpl
import com.ulyup.tierlist.domain.tierlist.repository.ItemRepository
import com.ulyup.tierlist.domain.tierlist.repository.TierlistRepository
import com.ulyup.tierlist.domain.tierlist.usecase.CreateItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.CreateTierlistUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.DeleteItemUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetMyTierlistsUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetPublicTierlistsUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.GetTierlistDetailUseCase
import com.ulyup.tierlist.domain.tierlist.usecase.MoveItemUseCase
import com.ulyup.tierlist.feature.feed.vm.FeedViewModel
import com.ulyup.tierlist.feature.mylists.vm.MyListsViewModel
import com.ulyup.tierlist.feature.tierlist_detail.vm.TierlistDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tierlistModule = module {
    singleOf(::TierlistApiImpl) bind TierlistApi::class
    singleOf(::ItemApiImpl) bind ItemApi::class
    singleOf(::TierlistRepositoryImpl) bind TierlistRepository::class
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class
    factoryOf(::GetPublicTierlistsUseCase)
    factoryOf(::GetMyTierlistsUseCase)
    factoryOf(::CreateTierlistUseCase)
    factoryOf(::GetTierlistDetailUseCase)
    factoryOf(::CreateItemUseCase)
    factoryOf(::DeleteItemUseCase)
    factoryOf(::MoveItemUseCase)
    viewModelOf(::FeedViewModel)
    viewModelOf(::MyListsViewModel)
    viewModelOf(::TierlistDetailViewModel)
}
