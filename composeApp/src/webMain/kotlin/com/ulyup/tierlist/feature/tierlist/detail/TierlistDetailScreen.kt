package com.ulyup.tierlist.feature.tierlist.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.components.state.StatefulContent
import com.ulyup.tierlist.core.ui.components.topbar.AppTopAppBar
import com.ulyup.tierlist.core.ui.token.gap8
import com.ulyup.tierlist.core.ui.token.gap16
import com.ulyup.tierlist.core.ui.token.paddingV16H24
import com.ulyup.tierlist.feature.tierlist.detail.components.TierRow
import com.ulyup.tierlist.feature.tierlist.detail.components.UnrankedStrip
import com.ulyup.tierlist.feature.tierlist.detail.vm.LoadDetailAction
import com.ulyup.tierlist.feature.tierlist.detail.vm.TierlistDetailState
import com.ulyup.tierlist.feature.tierlist.detail.vm.TierlistDetailViewModel
import com.ulyup.tierlist.model.Tier
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.detail_action_back
import com.ulyup.tierlist.resources.detail_empty
import com.ulyup.tierlist.resources.error_action_retry
import com.ulyup.tierlist.resources.ic_arrow_back
import com.ulyup.tierlist.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TierlistDetailScreen(
    tierlistId: Int,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<TierlistDetailViewModel> { parametersOf(tierlistId) }
    val state = viewModel.uiState
    val hasLoaded = state.itemsByTier.isNotEmpty()

    AppScaffold(
        topBar = {
            AppTopAppBar(
                title = state.title,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = stringResource(Res.string.detail_action_back),
                            tint = appColors.onSurface,
                        )
                    }
                },
            )
        },
    ) { padding ->
        StatefulContent(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isInitialLoad = !hasLoaded,
            isEmpty = hasLoaded && isFullyEmpty(state),
            emptyMessage = stringResource(Res.string.detail_empty),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = { viewModel.onAction(LoadDetailAction) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { contentModifier ->
            DetailContent(state = state, modifier = contentModifier)
        }
    }
}

@Composable
private fun DetailContent(state: TierlistDetailState, modifier: Modifier) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingV16H24),
        verticalArrangement = Arrangement.spacedBy(gap8),
    ) {
        Tier.entries.forEach { tier ->
            TierRow(tier = tier, items = state.itemsByTier[tier].orEmpty())
        }
        if (state.unrankedItems.isNotEmpty()) {
            UnrankedStrip(
                items = state.unrankedItems,
                modifier = Modifier.padding(top = gap16),
            )
        }
    }
}

private fun isFullyEmpty(state: TierlistDetailState): Boolean =
    state.unrankedItems.isEmpty() && state.itemsByTier.values.all { it.isEmpty() }