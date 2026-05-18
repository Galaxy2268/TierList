package com.ulyup.tierlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.util.ApiException
import com.ulyup.tierlist.core.network.util.apiCall
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.theme.AppTheme
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {
    AppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = appColors.background,
        ) {
            PingSmokeTest()
        }
    }
}

@Composable
private fun PingSmokeTest() {
    val httpClient = koinInject<HttpClient>()
    val sessionManager = koinInject<SessionManager>()
    val sessionState by sessionManager.authState.collectAsStateWithLifecycle()

    var result by remember { mutableStateOf("Tap the button to ping ${Routes.Auth.ME}") }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(aPadding24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Session: $sessionState",
            style = appTypography.bodyMedium,
            color = appColors.onBackground,
        )
        VBox16
        Button(
            onClick = {
                scope.launch {
                    result = try {
                        val response = apiCall { httpClient.get(Routes.Auth.ME) }
                        sessionManager.authorize()
                        "OK (status ${response.status.value})"
                    } catch (apiException: ApiException) {
                        "${apiException::class.simpleName}: ${apiException.message}"
                    }
                }
            },
        ) {
            Text(text = "Ping /me")
        }
        VBox16
        Text(
            text = result,
            style = appTypography.bodyMedium,
            color = appColors.onSurface,
        )
    }
}
