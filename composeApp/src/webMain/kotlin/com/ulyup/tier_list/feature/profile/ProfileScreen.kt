package com.ulyup.tier_list.feature.profile

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ulyup.tier_list.core.ui.components.button.PrimaryButton
import com.ulyup.tier_list.core.ui.components.layout.CenteredColumn
import com.ulyup.tier_list.core.ui.components.scaffold.AppScaffold
import com.ulyup.tier_list.core.ui.token.VBox24
import com.ulyup.tier_list.feature.profile.vm.LogoutAction
import com.ulyup.tier_list.feature.profile.vm.ProfileViewModel
import com.ulyup.tier_list.resources.Res
import com.ulyup.tier_list.resources.profile_action_logout
import com.ulyup.tier_list.resources.profile_title
import com.ulyup.tier_list.theme.appColors
import com.ulyup.tier_list.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        CenteredColumn(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(Res.string.profile_title),
                style = appTypography.titleLarge,
                color = appColors.onBackground,
            )
            VBox24
            PrimaryButton(
                text = stringResource(Res.string.profile_action_logout),
                onClick = { viewModel.onAction(LogoutAction) },
                isLoading = state.isLoggingOut,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}