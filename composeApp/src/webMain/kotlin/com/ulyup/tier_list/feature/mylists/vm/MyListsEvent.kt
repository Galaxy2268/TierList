package com.ulyup.tier_list.feature.mylists.vm

sealed interface MyListsEvent

data class ShowErrorMessageEvent(val text: String?) : MyListsEvent
