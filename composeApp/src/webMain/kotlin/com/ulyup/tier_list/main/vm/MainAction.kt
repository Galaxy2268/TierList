package com.ulyup.tier_list.main.vm

sealed interface MainAction

data object RetryBootstrapAction : MainAction