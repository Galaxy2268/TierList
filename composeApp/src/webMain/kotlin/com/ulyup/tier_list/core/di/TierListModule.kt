package com.ulyup.tier_list.core.di

import com.ulyup.tier_list.data.tier_list.api.ItemApi
import com.ulyup.tier_list.data.tier_list.api.ItemApiImpl
import com.ulyup.tier_list.data.tier_list.api.TierListApi
import com.ulyup.tier_list.data.tier_list.api.TierListApiImpl
import com.ulyup.tier_list.data.tier_list.repository.ItemRepositoryImpl
import com.ulyup.tier_list.data.tier_list.repository.TierListRepositoryImpl
import com.ulyup.tier_list.domain.tier_list.repository.ItemRepository
import com.ulyup.tier_list.domain.tier_list.repository.TierListRepository
import com.ulyup.tier_list.domain.tier_list.usecase.CreateItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.CreateTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.DeleteTierListUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetMyTierListsUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetPublicTierListsUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.GetTierListDetailUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.MoveItemUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.SetTierListVisibilityUseCase
import com.ulyup.tier_list.domain.tier_list.usecase.UpdateTierListUseCase
import com.ulyup.tier_list.feature.feed.vm.FeedViewModel
import com.ulyup.tier_list.feature.mylists.vm.MyListsViewModel
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val tierListModule = module {
    singleOf(::TierListApiImpl) bind TierListApi::class
    singleOf(::ItemApiImpl) bind ItemApi::class
    singleOf(::TierListRepositoryImpl) bind TierListRepository::class
    singleOf(::ItemRepositoryImpl) bind ItemRepository::class
    factoryOf(::GetPublicTierListsUseCase)
    factoryOf(::GetMyTierListsUseCase)
    factoryOf(::CreateTierListUseCase)
    factoryOf(::GetTierListDetailUseCase)
    factoryOf(::UpdateTierListUseCase)
    factoryOf(::SetTierListVisibilityUseCase)
    factoryOf(::DeleteTierListUseCase)
    factoryOf(::CreateItemUseCase)
    factoryOf(::DeleteItemUseCase)
    factoryOf(::MoveItemUseCase)
    viewModelOf(::FeedViewModel)
    viewModelOf(::MyListsViewModel)
    viewModelOf(::TierListDetailViewModel)
}
