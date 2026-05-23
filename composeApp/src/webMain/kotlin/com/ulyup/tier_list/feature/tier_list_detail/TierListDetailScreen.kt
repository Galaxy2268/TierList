package com.ulyup.tier_list.feature.tier_list_detail

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.unit.IntOffset
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.components.state.StatefulContent
import com.ulyup.tier_list.core.ui.components.topbar.AppTopAppBar
import com.ulyup.tier_list.core.ui.token.gap8
import com.ulyup.tier_list.core.ui.token.gap16
import com.ulyup.tier_list.core.ui.token.paddingV16H24
import com.ulyup.tier_list.feature.tier_list_detail.components.AddItemDialog
import com.ulyup.tier_list.feature.tier_list_detail.components.TierListItem
import com.ulyup.tier_list.feature.tier_list_detail.components.TierRow
import com.ulyup.tier_list.feature.tier_list_detail.components.UnrankedStrip
import com.ulyup.tier_list.feature.tier_list_detail.util.DragState
import com.ulyup.tier_list.feature.tier_list_detail.util.findItem
import com.ulyup.tier_list.feature.tier_list_detail.vm.AddItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DeleteItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ImagePickedAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.DismissAddItemDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.LoadDetailAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.MoveItemAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.ShowAddItemDialogAction
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailState
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailViewModel
import com.ulyup.tier_list.model.Tier
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.detail_action_add_item_fab
import com.ulyup.tier_list.resources.detail_action_back
import com.ulyup.tier_list.resources.detail_empty
import com.ulyup.tier_list.resources.error_action_retry
import com.ulyup.tier_list.resources.ic_add
import com.ulyup.tier_list.resources.ic_arrow_back
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
                onDeleteItem = { itemId -> viewModel.onAction(DeleteItemAction(itemId)) },
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
}

@Composable
private fun DetailContent(
    state: TierListDetailState,
    dragState: DragState,
    onMove: (itemId: Int, tier: Tier?, position: Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    modifier: Modifier,
) {
    val currentState by rememberUpdatedState(state)
    val currentOnMove by rememberUpdatedState(onMove)

    LaunchedEffect(state.itemsByTier, state.unrankedItems) {
        val allIds = (state.itemsByTier.values.flatten() + state.unrankedItems)
            .mapTo(mutableSetOf()) { it.id }
        dragState.pruneItemBounds(allIds)
    }

    val draggedId = dragState.dragged?.item?.id
    val deleteHandler: ((Int) -> Unit)? = if (state.isOwner) onDeleteItem else null
    val itemPositionedHandler: ((Int, Rect) -> Unit)? =
        if (state.isOwner) dragState::setItemBounds else null

    val dragModifier = if (state.isOwner) {
        Modifier.pointerInput(Unit) {
            detectDragGestures(
                onDragStart = { localOffset ->
                    val windowPos = dragState.rootWindowOffset + localOffset
                    val itemId = dragState.itemIdAt(windowPos) ?: return@detectDragGestures
                    val item = currentState.findItem(itemId) ?: return@detectDragGestures
                    dragState.startDrag(item, windowPos)
                },
                onDrag = { change, _ ->
                    change.consume()
                    dragState.updatePointer(dragState.rootWindowOffset + change.position)
                },
                onDragEnd = {
                    val target = dragState.computeDropTarget(
                        currentState.itemsByTier,
                        currentState.unrankedItems,
                    )
                    val draggedItem = dragState.dragged?.item
                    dragState.endDrag()
                    if (draggedItem != null && target != null) {
                        currentOnMove(draggedItem.id, target.tier, target.position)
                    }
                },
                onDragCancel = { dragState.endDrag() },
            )
        }
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
            verticalArrangement = Arrangement.spacedBy(gap8),
        ) {
            Tier.entries.forEach { tier ->
                val items = state.itemsByTier[tier].orEmpty()
                    .let { if (draggedId != null) it.filterNot { item -> item.id == draggedId } else it }
                TierRow(
                    tier = tier,
                    items = items,
                    onDeleteItem = deleteHandler,
                    onRowPositioned = { rect -> dragState.setTierRowBounds(tier, rect) },
                    onItemPositioned = itemPositionedHandler,
                )
            }
            val unranked = state.unrankedItems
                .let { if (draggedId != null) it.filterNot { item -> item.id == draggedId } else it }
            if (state.isOwner || unranked.isNotEmpty()) {
                UnrankedStrip(
                    items = unranked,
                    onDeleteItem = deleteHandler,
                    onRowPositioned = { rect -> dragState.setUnrankedRowBounds(rect) },
                    onItemPositioned = itemPositionedHandler,
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
