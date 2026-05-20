package com.ulyup.tierlist.core.mvi

interface LoadableState<S : LoadableState<S>> {
    val isLoading: Boolean
    val errorMessage: String?

    fun copyLoadable(isLoading: Boolean, errorMessage: String?): S

    fun withLoading(): S = copyLoadable(isLoading = true, errorMessage = null)
    fun withLoaded(): S = copyLoadable(isLoading = false, errorMessage = null)
    fun withError(message: String?): S = copyLoadable(isLoading = false, errorMessage = message)
}