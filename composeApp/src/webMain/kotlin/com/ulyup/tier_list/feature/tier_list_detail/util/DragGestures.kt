package com.ulyup.tier_list.feature.tier_list_detail.util

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.ulyup.tier_list.feature.tier_list_detail.vm.TierListDetailState
import com.ulyup.tier_list.model.Tier

fun Modifier.tierListDragGestures(
    dragState: DragState,
    state: () -> TierListDetailState,
    onMove: (itemId: Int, tier: Tier?, position: Int) -> Unit,
): Modifier = pointerInput(Unit) {
    detectDragGestures(
        onDragStart = { localOffset ->
            val windowPos = dragState.rootWindowOffset + localOffset
            val itemId = dragState.itemIdAt(windowPos) ?: return@detectDragGestures
            val item = state().findItem(itemId) ?: return@detectDragGestures
            dragState.startDrag(item, windowPos)
        },
        onDrag = { change, _ ->
            change.consume()
            dragState.updatePointer(dragState.rootWindowOffset + change.position)
            val current = state()
            dragState.recomputeDropTarget(
                current.itemsByTier,
                current.unrankedItems,
            )
        },
        onDragEnd = {
            val target = dragState.dropTarget
            val draggedItem = dragState.dragged?.item
            dragState.endDrag()
            if (draggedItem != null && target != null) {
                onMove(draggedItem.id, target.tier, target.position)
            }
        },
        onDragCancel = { dragState.endDrag() },
    )
}
