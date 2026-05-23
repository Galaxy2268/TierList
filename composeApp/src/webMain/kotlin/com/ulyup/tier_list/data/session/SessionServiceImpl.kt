package com.ulyup.tier_list.data.session

import com.ulyup.tier_list.data.auth.mapper.toDomain
import com.ulyup.tier_list.data.user.api.UserApi
import com.ulyup.tier_list.domain.auth.model.User
import com.ulyup.tier_list.domain.error.ApiException
import com.ulyup.tier_list.domain.session.SessionService
import com.ulyup.tier_list.domain.session.SessionState
import kotlinx.coroutines.flow.StateFlow

class SessionServiceImpl(
    private val userApi: UserApi,
    private val sessionManager: SessionManager,
) : SessionService {

    override val sessionState: StateFlow<SessionState> = sessionManager.authState
    override val currentUser: StateFlow<User?> = sessionManager.currentUser

    override suspend fun bootstrap() {
        sessionManager.unknown()
        try {
            val user = userApi.me().toDomain()
            sessionManager.signIn(user)
        } catch (_: ApiException.Unauthorized) {
            // HttpClient validator already flipped session to Unauthorized on 401.
        } catch (_: ApiException) {
            sessionManager.error()
        } catch (_: Throwable) {
            sessionManager.error()
        }
    }
}
