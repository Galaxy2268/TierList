package com.ulyup.tier_list.feature.shared.tier_list.vm

sealed interface TierListOptionsAction

data class ShowDeleteAction(val target: TierListOptionTarget) : TierListOptionsAction
data object DismissDeleteAction : TierListOptionsAction
data object ConfirmDeleteAction : TierListOptionsAction

data class ShowClearAction(val target: TierListOptionTarget) : TierListOptionsAction
data object DismissClearAction : TierListOptionsAction
data object ConfirmClearAction : TierListOptionsAction

data class ShowRenameAction(val target: TierListOptionTarget) : TierListOptionsAction
data object DismissRenameAction : TierListOptionsAction
value class ChangeRenameTitleAction(val value: String) : TierListOptionsAction
data object ConfirmRenameAction : TierListOptionsAction

data class ToggleVisibilityAction(val target: TierListOptionTarget) : TierListOptionsAction
data object DismissSharePrivateWarningAction : TierListOptionsAction
data object ConfirmMakePublicAction : TierListOptionsAction

data class ToggleFavouriteAction(val target: TierListOptionTarget) : TierListOptionsAction

data class ShareAction(val target: TierListOptionTarget) : TierListOptionsAction

data class ShowCopyAction(val target: TierListOptionTarget) : TierListOptionsAction
data object DismissCopyAction : TierListOptionsAction
data object ConfirmCopyAction : TierListOptionsAction
data object DismissPremiumLimitAction : TierListOptionsAction
data object UpgradeAndCopyAction : TierListOptionsAction
