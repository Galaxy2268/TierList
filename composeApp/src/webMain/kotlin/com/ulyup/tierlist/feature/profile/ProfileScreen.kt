package com.ulyup.tierlist.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.core.ui.components.button.PrimaryButton
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.token.VBox24
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.core.ui.token.maxFormWidth
import com.ulyup.tierlist.feature.profile.vm.LogoutAction
import com.ulyup.tierlist.feature.profile.vm.ProfileViewModel
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.profile_action_logout
import com.ulyup.tierlist.resources.profile_title
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(aPadding24),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
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
                modifier = Modifier.fillMaxWidth().widthIn(max = maxFormWidth),
            )
        }
    }
}
