package com.ulyup.tier_list.domain.tier_list.model

data class BatchUploadResult(
    val created: List<TierListItem>,
    val failedFilenames: List<String>,
)
