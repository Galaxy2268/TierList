package com.ulyup.tierlist.feature.mylists

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.components.topbar.AppTopAppBar
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography

@Composable
fun MyListsScreen() {
    AppScaffold(
        topBar = { AppTopAppBar(title = "TierRank") },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(aPadding24),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "My Lists (placeholder)",
                style = appTypography.titleLarge,
                color = appColors.onBackground,
            )
        }
    }
}
