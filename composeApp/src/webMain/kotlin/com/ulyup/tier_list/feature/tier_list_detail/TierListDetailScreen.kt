package com.ulyup.tier_list.feature.tier_list_detail

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ulyup.tier_list.core.mvi.ObserveAsEvents
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.components.state.StatefulContent
import com.ulyup.tier_list.core.ui.components.tier_list.DeleteTierListConfirmDialog
import com.ulyup.tier_list.core.ui.components.topbar.AppTopAppBar
import com.ulyup.tier_list.core.ui.token.gap16
import com.ulyup.tier_list.core.ui.token.paddingV16H24
import com.ulyup.tier_list.feature.tier_list_detail.components.AddItemDialog
import com.ulyup.tier_list.feature.tier_list_detail.components.RenameTierListDialog
import com.ulyup.tier_list.feature.tier_list_detail.components.TierListItem
import com.ulyup.tier_list.feature.tier_list_detail.components.TierRow
import com.ulyup.tier_list.feature.tier_list_detail.components.UnrankedStrip
import com.ulyup.tier_list.feature.tier_list_detail.util.DragState
import com.ulyup.tier_list.feature.tier_list_detail.util.tierListDragGestures
import com.ulyup.tier_list.feature.tier_list_detail.vm.AddItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ChangeRenameTitleAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ConfirmDeleteAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ConfirmRenameAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DeleteItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DismissAddItemDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DismissDeleteConfirmAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DismissRenameDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ImagePickedAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.LoadDetailAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.MoveItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ShowAddItemDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ShowDeleteConfirmAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ShowRenameDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDeletedEvent
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailState
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailViewModel
import com.ulyup.tier_list.feature.tier_list_detail.vm.ToggleVisibilityAction
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_add_item_fab
import com.ulyup.tier_list.resources.detail_action_back
import com.ulyup.tier_list.resources.detail_action_delete
import com.ulyup.tier_list.resources.detail_action_make_private
import com.ulyup.tier_list.resources.detail_action_make_public
import com.ulyup.tier_list.resources.detail_action_rename
import com.ulyup.tier_list.resources.detail_empty
import com.ulyup.tier_list.resources.error_action_retry
import com.ulyup.tier_list.resources.ic_add
import com.ulyup.tier_list.resources.ic_arrow_back
import com.ulyup.tier_list.resources.ic_delete
import com.ulyup.tier_list.resources.ic_edit
import com.ulyup.tier_list.resources.ic_unvisible
import com.ulyup.tier_list.resources.ic_visible
import com.ulyup.tier_list.theme.appColors
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TierListDetailScreen(
    tierListId: Int,
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<TierListDetailViewModel> { parametersOf(tierListId) }
    val state = viewModel.uiState
    val dragState = remember { DragState() }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            TierListDeletedEvent -> onBack()
        }
    }

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
                actions = {
                    if (state.isOwner) {
                        val visibilityIcon = if (state.isPublic) Res.drawable.ic_visible else Res.drawable.ic_unvisible
                        val visibilityLabel = if (state.isPublic) {
                            stringResource(Res.string.detail_action_make_private)
                        } else {
                            stringResource(Res.string.detail_action_make_public)
                        }
                        IconButton(
                            onClick = { viewModel.onAction(ToggleVisibilityAction) },
                            enabled = !state.isUpdatingVisibility,
                        ) {
                            Icon(
                                painter = painterResource(visibilityIcon),
                                contentDescription = visibilityLabel,
                                tint = appColors.onSurface,
                            )
                        }
                        IconButton(onClick = { viewModel.onAction(ShowRenameDialogAction) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_edit),
                                contentDescription = stringResource(Res.string.detail_action_rename),
                                tint = appColors.onSurface,
                            )
                        }
                        IconButton(onClick = { viewModel.onAction(ShowDeleteConfirmAction) }) {
                            Icon(
                                painter = painterResource(Res.drawable.ic_delete),
                                contentDescription = stringResource(Res.string.detail_action_delete),
                                tint = appColors.error,
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (state.isOwner) {
                FloatingActionButton(onClick = { viewModel.onAction(ShowAddItemDialogAction) }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_add),
                        contentDescription = stringResource(Res.string.detail_action_add_item_fab),
                    )
                }
            }
        },
    ) { padding ->
        StatefulContent(
            isLoading = state.isLoading,
            errorMessage = state.errorMessage,
            isEmpty = !state.isOwner && isFullyEmpty(state),
            emptyMessage = stringResource(Res.string.detail_empty),
            retryLabel = stringResource(Res.string.error_action_retry),
            onRetry = { viewModel.onAction(LoadDetailAction) },
            modifier = Modifier.fillMaxSize().padding(padding),
        ) { contentModifier ->
            DetailContent(
                state = state,
                dragState = dragState,
                onMove = { itemId, tier, position ->
                    viewModel.onAction(MoveItemAction(itemId, tier, position))
                },
                onDeleteItem = if (state.isOwner) {
                    { itemId -> viewModel.onAction(DeleteItemAction(itemId)) }
                } else {
                    null
                },
                onItemPositioned = if (state.isOwner) dragState::setItemBounds else null,
                modifier = contentModifier,
            )
        }
    }

    state.addItemDialog?.let { dialog ->
        AddItemDialog(
            state = dialog,
            onImagePicked = { bytes, filename -> viewModel.onAction(ImagePickedAction(bytes, filename)) },
            onConfirm = { viewModel.onAction(AddItemAction) },
            onDismiss = { viewModel.onAction(DismissAddItemDialogAction) },
        )
    }

    state.renameDialog?.let { dialog ->
        RenameTierListDialog(
            state = dialog,
            onTitleChange = { viewModel.onAction(ChangeRenameTitleAction(it)) },
            onConfirm = { viewModel.onAction(ConfirmRenameAction) },
            onDismiss = { viewModel.onAction(DismissRenameDialogAction) },
        )
    }

    if (state.isDeleteConfirmVisible) {
        DeleteTierListConfirmDialog(
            tierListTitle = state.title,
            onConfirm = { viewModel.onAction(ConfirmDeleteAction) },
            onDismiss = { viewModel.onAction(DismissDeleteConfirmAction) },
            isLoading = state.isDeleting,
            errorMessage = state.deleteErrorMessage,
        )
    }
}

@Composable
private fun DetailContent(
    state: TierListDetailState,
    dragState: DragState,
    onMove: (itemId: Int, tier: Tier?, position: Int) -> Unit,
    onDeleteItem: ((Int) -> Unit)?,
    onItemPositioned: ((Int, Rect) -> Unit)?,
    modifier: Modifier,
) {
    val currentState by rememberUpdatedState(state)
    val currentOnMove by rememberUpdatedState(onMove)

    LaunchedEffect(state.itemsByTier, state.unrankedItems) {
        val allIds = (state.itemsByTier.values.flatten() + state.unrankedItems)
            .mapTo(mutableSetOf()) { it.id }
        dragState.pruneItemBounds(allIds)
    }

    val draggedItem = dragState.dragged?.item
    val draggedId = draggedItem?.id
    val dropTarget = dragState.dropTarget
    val ghostImageUrl = if (dropTarget != null) draggedItem?.imageUrl else null

    val dragModifier = if (state.isOwner) {
        Modifier.tierListDragGestures(
            dragState = dragState,
            state = { currentState },
            onMove = { id, tier, position -> currentOnMove(id, tier, position) },
        )
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .onGloballyPositioned { dragState.setRootOffset(it.positionInWindow()) }
            .then(dragModifier),
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingV16H24),
        ) {
            Column(
                modifier = Modifier.border(2.dp, appColors.background),
            ) {
                Tier.entries.forEachIndexed { index, tier ->
                    if (index > 0) {
                        HorizontalDivider(
                            thickness = 2.dp,
                            color = appColors.background,
                        )
                    }
                    val tierItems = state.itemsByTier[tier].orEmpty()
                    val items = remember(tierItems, draggedId) {
                        if (draggedId != null) tierItems.filterNot { item -> item.id == draggedId } else tierItems
                    }
                    TierRow(
                        tier = tier,
                        items = items,
                        onDeleteItem = onDeleteItem,
                        onRowPositioned = { rect -> dragState.setTierRowBounds(tier, rect) },
                        onItemPositioned = onItemPositioned,
                        ghostImageUrl = ghostImageUrl,
                        ghostIndex = dropTarget?.takeIf { it.tier == tier }?.position,
                    )
                }
            }
            val unrankedItems = state.unrankedItems
            val unranked = remember(unrankedItems, draggedId) {
                if (draggedId != null) unrankedItems.filterNot { item -> item.id == draggedId } else unrankedItems
            }
            if (state.isOwner || unranked.isNotEmpty()) {
                UnrankedStrip(
                    items = unranked,
                    onDeleteItem = onDeleteItem,
                    onRowPositioned = { rect -> dragState.setUnrankedRowBounds(rect) },
                    onItemPositioned = onItemPositioned,
                    ghostImageUrl = ghostImageUrl,
                    ghostIndex = dropTarget?.takeIf { it.tier == null }?.position,
                    modifier = Modifier.padding(top = gap16),
                )
            }
        }

        dragState.dragged?.let { dragged ->
            Box(
                modifier = Modifier.offset {
                    val pos = dragState.pointerWindowPosition - dragged.grabOffset - dragState.rootWindowOffset
                    IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
                },
            ) {
                TierListItem(item = dragged.item)
            }
        }
    }
}

private fun isFullyEmpty(state: TierListDetailState): Boolean =
    state.unrankedItems.isEmpty() && state.itemsByTier.values.all { it.isEmpty() }
