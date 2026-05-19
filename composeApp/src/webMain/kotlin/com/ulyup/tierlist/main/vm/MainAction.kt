package com.ulyup.tierlist.main.vm

sealed interface MainAction

data object RetryBootstrapAction : MainAction