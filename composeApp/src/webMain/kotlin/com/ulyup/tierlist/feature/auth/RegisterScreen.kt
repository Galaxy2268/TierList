package com.ulyup.tierlist.feature.auth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.ulyup.tierlist.core.ui.components.layout.CenteredColumn
import com.ulyup.tierlist.core.ui.components.text.AppTextField
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.VBox24
import com.ulyup.tierlist.feature.auth.components.AuthPasswordSection
import com.ulyup.tierlist.feature.auth.vm.register.ChangeEmailAction
import com.ulyup.tierlist.feature.auth.vm.register.ChangePasswordAction
import com.ulyup.tierlist.feature.auth.vm.register.ChangeUsernameAction
import com.ulyup.tierlist.feature.auth.vm.register.RegisterViewModel
import com.ulyup.tierlist.feature.auth.vm.register.SubmitAction
import com.ulyup.tierlist.resources.Res
import com.ulyup.tierlist.resources.register_action_back
import com.ulyup.tierlist.resources.register_action_submit
import com.ulyup.tierlist.resources.register_field_email
import com.ulyup.tierlist.resources.register_field_password
import com.ulyup.tierlist.resources.register_field_username
import com.ulyup.tierlist.resources.register_title
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterScreen(
    onBack: () -> Unit,
) {
    val viewModel = koinViewModel<RegisterViewModel>()
    val state = viewModel.uiState

    AppScaffold { padding ->
        CenteredColumn(modifier = Modifier.padding(padding)) {
            Text(
                text = stringResource(Res.string.register_title),
                style = appTypography.titleLarge,
                color = appColors.onBackground,
            )
            VBox24

            AppTextField(
                value = state.username,
                onValueChange = { viewModel.onAction(ChangeUsernameAction(it)) },
                label = stringResource(Res.string.register_field_username),
                enabled = !state.isLoading,
            )
            VBox16

            AppTextField(
                value = state.email,
                onValueChange = { viewModel.onAction(ChangeEmailAction(it)) },
                label = stringResource(Res.string.register_field_email),
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            )

            AuthPasswordSection(
                password = state.password,
                onPasswordChange = { viewModel.onAction(ChangePasswordAction(it)) },
                passwordLabel = stringResource(Res.string.register_field_password),
                submitLabel = stringResource(Res.string.register_action_submit),
                onSubmit = { viewModel.onAction(SubmitAction) },
                secondaryLabel = stringResource(Res.string.register_action_back),
                onSecondaryClick = onBack,
                errorMessage = state.errorMessage,
                isLoading = state.isLoading,
            )
        }
    }
}