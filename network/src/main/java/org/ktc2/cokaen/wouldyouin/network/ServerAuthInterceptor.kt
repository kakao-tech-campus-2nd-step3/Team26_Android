package org.ktc2.cokaen.wouldyouin.network

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import javax.inject.Inject

class ServerAuthInterceptor @Inject constructor(
    private val authPrefs: AuthPreferenceManager,
    @ApplicationContext private val context: Context,
    private val navigationUtil: NavigationUtil
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val token = authPrefs.token
        val authenticatedRequest = if (!token.isNullOrEmpty()) {
            request.newBuilder()
                .addHeader("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJtZW1iZXJJZCI6MiwibWVtYmVyVHlwZSI6ImN1cmF0b3IiLCJpYXQiOjE3MzE4Mjk0OTMsImV4cCI6MTgxODIyOTQ5M30.pZ9OZEsAgJGc1zlx6CDn18_djavCsp4VTawJiLla6q0")
                .build()
        } else {
            request
        }

        val response = chain.proceed(authenticatedRequest)

        // 401(Unauthorized) 에러 발생 시
        if (response.code == 401) {
            authPrefs.clearAll()  // 모든 사용자 정보 삭제

            Handler(Looper.getMainLooper()).post {
                navigationUtil.navigate(
                    NavigationCommand(
                        destination = NavigationDestination.Activity(DeepLinkDestinations.MAIN_ACTIVITY),
                        activityOptions = ActivityNavigationOptions(
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        ),
                        data = mapOf("token_expired" to true.toString())
                    )
                )
            }
        }

        return response
    }
}