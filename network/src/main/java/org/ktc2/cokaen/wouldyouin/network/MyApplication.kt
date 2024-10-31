package org.ktc2.cokaen.wouldyouin

import android.app.Application
import com.kakao.vectormap.KakaoMapSdk
import org.ktc2.cokaen.wouldyouin.network.R

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, getString(R.string.kakao_api_key))
    }
}