package com.ulyup.tier_list.feature.tier_list_detail.vm

sealed interface TierListDetailEvent

data class ShowErrorMessageEvent(val text: String?) : TierListDetailEvent
data class ShowUploadFailuresEvent(val filenames: List<String>) : TierListDetailEvent
