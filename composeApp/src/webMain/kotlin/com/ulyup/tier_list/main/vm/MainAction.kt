package com.ulyup.tier_list.main.vm

sealed interface MainAction

data object RetryBootstrapAction : MainAction

data class SaveLastTabAction(val graphName: String) : MainAction

data class SaveLastDetailAction(val tierListId: Int) : MainAction

data object ClearLastDetailAction : MainAction
