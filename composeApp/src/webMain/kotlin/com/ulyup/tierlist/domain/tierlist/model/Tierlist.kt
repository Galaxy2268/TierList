package com.ulyup.tierlist.domain.tierlist.model

data class Tierlist(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val isPublic: Boolean,
    val createdAt: Long,
)
