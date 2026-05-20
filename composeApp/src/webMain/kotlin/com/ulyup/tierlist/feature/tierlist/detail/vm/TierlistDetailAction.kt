package com.ulyup.tierlist.feature.tierlist.detail.vm

sealed interface TierlistDetailAction

data object LoadDetailAction : TierlistDetailAction
