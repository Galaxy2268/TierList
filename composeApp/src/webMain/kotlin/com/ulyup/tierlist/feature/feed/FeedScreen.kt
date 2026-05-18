package com.ulyup.tierlist.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ulyup.tierlist.Routes
import com.ulyup.tierlist.core.network.session.SessionManager
import com.ulyup.tierlist.core.network.util.ApiException
import com.ulyup.tierlist.core.network.util.apiCall
import com.ulyup.tierlist.core.ui.token.VBox16
import com.ulyup.tierlist.core.ui.token.aPadding24
import com.ulyup.tierlist.theme.appColors
import com.ulyup.tierlist.theme.appTypography
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun FeedScreen() {
    val httpClient = koinInject<HttpClient>()
    val sessionManager = koinInject<SessionManager>()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(aPadding24),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Feed (placeholder)",
            style = appTypography.titleLarge,
            color = appColors.onBackground,
        )
        VBox16
        Button(
            onClick = {
                scope.launch {
                    try {
                        apiCall { httpClient.post(Routes.Auth.LOGOUT) }
                    } catch (_: ApiException) {
                    }
                    sessionManager.unauthorize()
                }
            },
        ) {
            Text(text = "Logout (temp)")
        }
    }
}
