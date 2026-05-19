package com.ulyup.tierlist.feature.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.ulyup.tierlist.core.ui.components.text.AppTextField
import com.ulyup.tierlist.core.ui.components.text.ErrorText
import com.ulyup.tierlist.core.ui.components.button.PrimaryButton
import com.ulyup.tierlist.core.ui.components.scaffold.AppScaffold
import com.ulyup.tierlist.core.ui.token.VBox8
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.VBox24
import com.ulyup.tierlist.feature.auth.components.AuthCenteredColumn
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
        AuthCenteredColumn(modifier = Modifier.padding(padding)) {
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
            VBox16

            AppTextField(
                value = state.password,
                onValueChange = { viewModel.onAction(ChangePasswordAction(it)) },
                label = stringResource(Res.string.register_field_password),
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = PasswordVisualTransformation(),
            )
            VBox16

            PrimaryButton(
                text = stringResource(Res.string.register_action_submit),
                onClick = { viewModel.onAction(SubmitAction) },
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth(),
            )

            state.errorMessage?.let { message ->
                VBox8
                ErrorText(message = message)
            }

            VBox8
            TextButton(
                onClick = onBack,
                enabled = !state.isLoading,
            ) {
                Text(
                    text = stringResource(Res.string.register_action_back),
                    style = appTypography.labelLarge,
                    color = appColors.primary,
                )
            }
        }
    }
}