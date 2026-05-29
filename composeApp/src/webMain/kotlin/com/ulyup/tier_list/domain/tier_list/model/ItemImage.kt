package com.ulyup.tier_list.domain.tier_list.model

@Suppress("ArrayInDataClass")
data class ItemImage(
    val bytes: ByteArray,
    val filename: String,
)
