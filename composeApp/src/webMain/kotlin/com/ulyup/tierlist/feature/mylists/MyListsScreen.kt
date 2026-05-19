package com.ulyup.tierlist.feature.mylists

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.components.state.EmptyState
import com.ulyup.tierlist.core.ui.components.state.ErrorState
import com.ulyup.tierlist.core.ui.components.state.LoadingState
import com.ulyup.tierlist.core.ui.components.tierlist.TierlistCard
import com.ulyup.tierlist.core.ui.token.paddingV16H24
import com.ulyup.tierlist.core.ui.token.tierlistCardMinWidth
import com.ulyup.tierlist.feature.mylists.components.CreateTierlistDialog
import com.ulyup.tierlist.feature.mylists.components.PremiumUpsellCard
import com.ulyup.tierlist.feature.mylists.vm.ChangeCreateTitleAction
import com.ulyup.tierlist.feature.mylists.vm.ConfirmCreateAction
import com.ulyup.tierlist.feature.mylists.vm.DismissCreateDialogAction
import com.ulyup.tierlist.feature.mylists.vm.LoadMyListsAction
import com.ulyup.tierlist.feature.mylists.vm.MyListsViewModel
import com.ulyup.tierlist.feature.mylists.vm.ShowCreateDialogAction
import com.ulyup.tierlist.feature.mylists.vm.ToggleCreatePublicAction
import com.ulyup.tierlist.feature.mylists.vm.UpgradePremiumAction
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.error_action_retry
import com.ulyup.tierlist.resources.mylists_action_create_label
import com.ulyup.tierlist.resources.mylists_empty
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyListsScreen() {
    val viewModel = koinViewModel<MyListsViewModel>()
    val state = viewModel.uiState

    AppScaffold(
        floatingActionButton = {
            if (state.showCreateFab) {
                FloatingActionButton(onClick = { viewModel.onAction(ShowCreateDialogAction) }) {
                    Text(text = stringResource(Res.string.mylists_action_create_label))
                }
            }
        },
    ) { padding ->
        val modifier = Modifier.fillMaxSize().padding(padding)
        when {
            state.isLoading && state.isInitialLoad ->
                LoadingState(modifier = modifier)

            state.errorMessage != null && state.isInitialLoad ->
                ErrorState(
                    message = state.errorMessage,
                    modifier = modifier,
                    retryLabel = stringResource(Res.string.error_action_retry),
                    onRetry = { viewModel.onAction(LoadMyListsAction) },
                )

            state.tierlists.isEmpty() && !state.isAtCap ->
                EmptyState(
                    message = stringResource(Res.string.mylists_empty),
                    modifier = modifier,
                )

            else -> LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = tierlistCardMinWidth),
                modifier = modifier,
                contentPadding = paddingV16H24,
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.tierlists, key = { it.id }) { tierlist ->
                    TierlistCard(tierlist = tierlist)
                }
                if (state.isAtCap) {
                    item(key = "premium-upsell", span = { GridItemSpan(maxLineSpan) }) {
                        PremiumUpsellCard(
                            isUpgrading = state.isUpgrading,
                            onUpgrade = { viewModel.onAction(UpgradePremiumAction) },
                        )
                    }
                }
            }
        }
    }

    state.createDialog?.let { dialog ->
        CreateTierlistDialog(
            state = dialog,
            onTitleChange = { viewModel.onAction(ChangeCreateTitleAction(it)) },
            onPublicChange = { viewModel.onAction(ToggleCreatePublicAction(it)) },
            onConfirm = { viewModel.onAction(ConfirmCreateAction) },
            onDismiss = { viewModel.onAction(DismissCreateDialogAction) },
        )
    }
}
