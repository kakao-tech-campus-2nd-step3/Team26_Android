package org.ktc2.cokaen.wouldyouin.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ServerAuthInterceptor @Inject constructor(
    private val authPrefs: AuthPreferenceManager  // AuthPreferenceManager 주입받음
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = authPrefs.token  // AuthPreferenceManager에서 저장된 토큰 가져옴

        val request = if (!token.isNullOrEmpty()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")  // 토큰을 헤더에 추가
                .build()
        } else {
            chain.request()  // 토큰이 없으면 원래 요청 그대로
        }

        return chain.proceed(request)
    }
}