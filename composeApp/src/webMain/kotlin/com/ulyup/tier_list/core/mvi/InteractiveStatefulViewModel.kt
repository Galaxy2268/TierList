package com.ulyup.tier_list.core.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class InteractiveStatefulViewModel<A : Any, S : Any, E : Any>(
    initialState: S,
) : StatefulViewModel<A, S>(initialState) {

    private val eventChannel = Channel<E>(Channel.BUFFERED)
    val events: Flow<E> = eventChannel.receiveAsFlow()

    protected suspend fun sendEvent(event: E) {
        eventChannel.send(event)
    }

    protected fun launchEvent(event: E) {
        viewModelScope.launch { sendEvent(event) }
    }
}

@Composable
fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
    LaunchedEffect(flow) { flow.collect(onEvent) }
}