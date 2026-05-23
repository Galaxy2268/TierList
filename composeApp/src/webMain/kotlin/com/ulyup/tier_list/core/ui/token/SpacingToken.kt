package com.ulyup.tier_list.core.ui.token

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Gap values (for spacedBy / one-off padding sides)
val gap4 = 4.dp
val gap8 = 8.dp
val gap12 = 12.dp
val gap16 = 16.dp
val gap24 = 24.dp

// Vertical spacers (height)
val VBox4 @Composable get() = Spacer(modifier = Modifier.height(4.dp))
val VBox8 @Composable get() = Spacer(modifier = Modifier.height(8.dp))
val VBox12 @Composable get() = Spacer(modifier = Modifier.height(12.dp))
val VBox16 @Composable get() = Spacer(modifier = Modifier.height(16.dp))
val VBox24 @Composable get() = Spacer(modifier = Modifier.height(24.dp))
val VBox32 @Composable get() = Spacer(modifier = Modifier.height(32.dp))

// Horizontal spacers (width)
val HBox4 @Composable get() = Spacer(modifier = Modifier.width(4.dp))
val HBox8 @Composable get() = Spacer(modifier = Modifier.width(8.dp))
val HBox12 @Composable get() = Spacer(modifier = Modifier.width(12.dp))
val HBox16 @Composable get() = Spacer(modifier = Modifier.width(16.dp))

// Weight-based fillers (push siblings to the edges)
val ColumnScope.DefaultSpacer @Composable get() = Spacer(modifier = Modifier.weight(1f))
val RowScope.DefaultSpacer @Composable get() = Spacer(modifier = Modifier.weight(1f))
