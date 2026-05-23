package com.ulyup.tier_list.core.ui.components.topbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    title: String,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = appTypography.titleLarge,
            )
        },
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appColors.surface,
            titleContentColor = appColors.onSurface,
        ),
    )
}