package com.ulyup.tier_list.feature.shared.tier_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.ulyup.tier_list.core.mvi.ObserveAsEvents
import com.ulyup.tier_list.core.ui.snackbar.LocalTierListSnackbarHandler
import com.ulyup.tier_list.feature.shared.tier_list.components.ClearItemsConfirmDialog
import com.ulyup.tier_list.feature.shared.tier_list.components.DeleteTierListConfirmDialog
import com.ulyup.tier_list.feature.shared.tier_list.components.RenameTierListDialog
import com.ulyup.tier_list.feature.shared.tier_list.components.SharePrivateWarningDialog
import com.ulyup.tier_list.feature.shared.tier_list.vm.ChangeRenameTitleAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ConfirmClearAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ConfirmDeleteAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ConfirmMakePublicAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.ConfirmRenameAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.DismissClearAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.DismissDeleteAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.DismissRenameAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.DismissSharePrivateWarningAction
import com.ulyup.tier_list.feature.shared.tier_list.vm.FavouriteChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.ItemsClearedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShareLinkCopiedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.ShowErrorMessageEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionsEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListOptionsViewModel
import com.ulyup.tier_list.feature.shared.tier_list.vm.TierListDeletedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.TitleChangedEvent
import com.ulyup.tier_list.feature.shared.tier_list.vm.VisibilityChangedEvent
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.share_link_copied_toast
import kotlinx.coroutines.launch

@Composable
fun TierListOptionsHost(
    viewModel: TierListOptionsViewModel,
    onEvent: (TierListOptionsEvent) -> Unit,
) {
    val state = viewModel.uiState
    val snackbarHandler = LocalTierListSnackbarHandler.current
    val scope = rememberCoroutineScope()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ShareLinkCopiedEvent -> scope.launch {
                snackbarHandler.showMessage(Res.string.share_link_copied_toast)
            }
            is ShowErrorMessageEvent -> scope.launch { snackbarHandler.showError(event.text) }
            is TierListDeletedEvent,
            is ItemsClearedEvent,
            is FavouriteChangedEvent,
            is VisibilityChangedEvent,
            is TitleChangedEvent -> onEvent(event)
        }
    }

    state.deleteConfirm?.let { pending ->
        DeleteTierListConfirmDialog(
            tierListTitle = pending.title,
            onConfirm = { viewModel.onAction(ConfirmDeleteAction) },
            onDismiss = { viewModel.onAction(DismissDeleteAction) },
            isLoading = pending.isLoading,
            errorMessage = pending.errorMessage,
        )
    }

    state.clearConfirm?.let { pending ->
        ClearItemsConfirmDialog(
            onConfirm = { viewModel.onAction(ConfirmClearAction) },
            onDismiss = { viewModel.onAction(DismissClearAction) },
            isLoading = pending.isLoading,
            errorMessage = pending.errorMessage,
        )
    }

    state.renameDialog?.let { dialog ->
        RenameTierListDialog(
            state = dialog,
            onTitleChange = { viewModel.onAction(ChangeRenameTitleAction(it)) },
            onConfirm = { viewModel.onAction(ConfirmRenameAction) },
            onDismiss = { viewModel.onAction(DismissRenameAction) },
        )
    }

    if (state.sharePrivateWarning != null) {
        SharePrivateWarningDialog(
            onMakePublic = { viewModel.onAction(ConfirmMakePublicAction) },
            onDismiss = { viewModel.onAction(DismissSharePrivateWarningAction) },
        )
    }
}
