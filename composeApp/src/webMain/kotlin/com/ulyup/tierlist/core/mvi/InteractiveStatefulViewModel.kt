package com.ulyup.tierlist.core.mvi

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class InteractiveStatefulViewModel<A : Any, S : Any, E : Any>(
    initialState: S,
) : StatefulViewModel<A, S>(initialState) {

    private val effectChannel = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = effectChannel.receiveAsFlow()

    protected suspend fun sendEffect(effect: E) {
        effectChannel.send(effect)
    }

    protected fun launchEffect(effect: E) {
        viewModelScope.launch { sendEffect(effect) }
    }
}