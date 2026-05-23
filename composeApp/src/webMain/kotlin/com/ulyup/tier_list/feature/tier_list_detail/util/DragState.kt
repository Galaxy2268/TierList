package com.ulyup.tier_list.feature.tier_list_detail.util

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import com.ulyup.tier_list.domain.tier_list.model.TierListItem
import com.ulyup.tier_list.model.Tier

@Stable
class DragState {
    var dragged: DraggedItemContext? by mutableStateOf(null)
        private set
    var pointerWindowPosition: Offset by mutableStateOf(Offset.Zero)
        private set
    var rootWindowOffset: Offset by mutableStateOf(Offset.Zero)
        private set

    private val itemBoundsMap: SnapshotStateMap<Int, Rect> = mutableStateMapOf()
    private val tierRowBoundsMap: SnapshotStateMap<Tier, Rect> = mutableStateMapOf()
    private var unrankedRowBounds: Rect? by mutableStateOf(null)

    fun setRootOffset(offset: Offset) {
        rootWindowOffset = offset
    }

    fun setItemBounds(itemId: Int, bounds: Rect) {
        itemBoundsMap[itemId] = bounds
    }

    fun setTierRowBounds(tier: Tier, bounds: Rect) {
        tierRowBoundsMap[tier] = bounds
    }

    fun setUnrankedRowBounds(bounds: Rect) {
        unrankedRowBounds = bounds
    }

    fun pruneItemBounds(keepIds: Set<Int>) {
        val stale = itemBoundsMap.keys.filter { it !in keepIds }
        stale.forEach { itemBoundsMap.remove(it) }
    }

    fun itemIdAt(windowPoint: Offset): Int? =
        itemBoundsMap.entries.firstOrNull { it.value.contains(windowPoint) }?.key

    fun startDrag(item: TierListItem, pointerWindowPos: Offset) {
        val itemBounds = itemBoundsMap[item.id] ?: return
        dragged = DraggedItemContext(
            item = item,
            grabOffset = pointerWindowPos - itemBounds.topLeft,
        )
        pointerWindowPosition = pointerWindowPos
    }

    fun updatePointer(windowPos: Offset) {
        pointerWindowPosition = windowPos
    }

    fun endDrag() {
        dragged = null
    }

    fun computeDropTarget(
        itemsByTier: Map<Tier, List<TierListItem>>,
        unrankedItems: List<TierListItem>,
    ): DropTarget? {
        val draggedItem = dragged?.item ?: return null
        val pointer = pointerWindowPosition

        val targetTier: Tier? = tierRowBoundsMap.entries
            .firstOrNull { it.value.contains(pointer) }?.key
        val isInUnranked = targetTier == null && unrankedRowBounds?.contains(pointer) == true
        if (targetTier == null && !isInUnranked) return null

        val targetItems = (if (targetTier != null) itemsByTier[targetTier].orEmpty() else unrankedItems)
            .filter { it.id != draggedItem.id }

        val insertIndex = targetItems.indexOfFirst { item ->
            val rect = itemBoundsMap[item.id] ?: return@indexOfFirst false
            pointer.y < rect.bottom && (pointer.y < rect.top || pointer.x < rect.center.x)
        }.let { if (it < 0) targetItems.size else it }

        return DropTarget(tier = targetTier, position = insertIndex)
    }
}

data class DraggedItemContext(
    val item: TierListItem,
    val grabOffset: Offset,
)

data class DropTarget(
    val tier: Tier?,
    val position: Int,
)
