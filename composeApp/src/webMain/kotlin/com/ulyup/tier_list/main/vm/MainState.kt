package com.ulyup.tier_list.main.vm

import com.ulyup.tier_list.feature.splash.navigation.SplashGraph

data class MainState(
    val startDestination: Any = SplashGraph,
    val initialDetailId: Int? = null,
)
