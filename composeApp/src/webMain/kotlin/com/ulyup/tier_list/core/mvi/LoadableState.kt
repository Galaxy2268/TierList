package com.ulyup.tier_list.core.mvi

interface LoadableState<S : LoadableState<S>> {
    val isLoading: Boolean
    val errorMessage: String?

    fun copyLoadable(isLoading: Boolean, errorMessage: String?): S

    fun withLoading(): S = copyLoadable(isLoading = true, errorMessage = null)
    fun withSuccess(): S = copyLoadable(isLoading = false, errorMessage = null)
    fun withError(message: String?): S = copyLoadable(isLoading = false, errorMessage = message)
}
