package com.ulyup.tierlist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform