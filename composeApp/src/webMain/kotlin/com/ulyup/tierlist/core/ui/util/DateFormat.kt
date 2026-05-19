package com.ulyup.tierlist.core.ui.util

import kotlin.time.Instant

fun Long.toIsoDate(): String =
    Instant.fromEpochMilliseconds(this).toString().take(10)
