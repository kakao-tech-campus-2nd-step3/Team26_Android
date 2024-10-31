package org.ktc2.cokaen.wouldyouin

import android.app.Application
import android.content.Context
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {

    lateinit var appContext: Context

    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
        appContext = applicationContext
    }
}