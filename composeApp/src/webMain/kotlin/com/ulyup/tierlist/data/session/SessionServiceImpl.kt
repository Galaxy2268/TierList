package com.ulyup.tierlist.data.session

import com.ulyup.tierlist.data.user.api.UserApi
import com.ulyup.tierlist.domain.error.ApiException
import com.ulyup.tierlist.domain.session.SessionService
import com.ulyup.tierlist.domain.session.SessionState
import kotlinx.coroutines.flow.StateFlow

class SessionServiceImpl(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
) : SessionService {

    override val sessionState: StateFlow<SessionState> = sessionManager.authState

    override suspend fun bootstrap() {
        sessionManager.unknown()
        try {
            userApi.me()
            sessionManager.authorize()
        } catch (_: ApiException.Unauthorized) {
            // HttpClient validator already flipped session to Unauthorized on 401.
        } catch (_: ApiException) {
            sessionManager.error()
        }
    }
}
