package com.ulyup.tierlist.feature.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.core.navigation.LocalAppNavigator
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.util.ApiException
import com.ulyup.tierlist.core.network.util.apiCall
import com.ulyup.tierlist.core.ui.token.VBox8
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.dto.LoginRequest
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun LoginScreen() {
    val httpClient = koinInject<HttpClient>()
    val sessionManager = koinInject<SessionManager>()
    val appNavigator = LocalAppNavigator.current
    val scope = rememberCoroutineScope()
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(aPadding24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Login (placeholder)",
            style = appTypography.titleLarge,
            color = appColors.onBackground,
        )
        VBox16
        Button(
            onClick = {
                scope.launch {
                    error = null
                    try {
                        apiCall {
                            httpClient.post(Routes.Auth.LOGIN) {
                                setBody(
                                    LoginRequest(
                                        usernameOrEmail = "testuser",
                                        password = "testpass123",
                                    )
                                )
                            }
                        }
                        sessionManager.authorize()
                    } catch (apiException: ApiException) {
                        error = "${apiException::class.simpleName}: ${apiException.message}"
                    }
                }
            },
        ) {
            Text(text = "Login as testuser (temp)")
        }
        VBox8
        Button(onClick = { appNavigator.toRegister() }) {
            Text(text = "Go to Register")
        }
        error?.let { message ->
            VBox8
            Text(
                text = message,
                style = appTypography.bodySmall,
                color = appColors.error,
            )
        }
    }
}