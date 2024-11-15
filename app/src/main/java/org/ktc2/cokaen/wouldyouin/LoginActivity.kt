package org.ktc2.cokaen.wouldyouin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import com.example.feat_onboarding.view.OnboardingActivity
import org.ktc2.cokaen.wouldyouin.BuildConfig.KAKAO_REDIRECT_URI
import org.ktc2.cokaen.wouldyouin.databinding.ActivityLoginBinding
import org.ktc2.cokaen.wouldyouin.network.repository.AuthRepository

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authRepository = AuthRepository()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.login = this

        // Kakao 로그인 버튼 클릭 시 WebView를 전체 화면으로 표시
        binding.kakaoLoginButton.setOnClickListener {
            binding.webview.apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        // 리다이렉트 URI 확인하여 인증 코드 처리
                        if (url.startsWith(KAKAO_REDIRECT_URI)) {
                            val uri = Uri.parse(url)
                            val code = uri.getQueryParameter("code")
                            if (code != null) {
                                binding.webview.visibility = View.GONE
                                fetchToken(code)
                            }
                            return true
                        }
                        return false
                    }
                }
                settings.javaScriptEnabled = true
                binding.profileImage.visibility = View.GONE
                binding.kakaoLoginButton.visibility = View.GONE
                binding.localLoginButton.visibility = View.GONE
                visibility = View.VISIBLE
                loadUrl("https://kauth.kakao.com/oauth/authorize?client_id=${BuildConfig.KAKAO_CLIENT_ID}&redirect_uri=${BuildConfig.KAKAO_REDIRECT_URI}&response_type=code")
            }
        }
    }

    // 인증 코드로 토큰을 가져오고 화면을 전환하는 메서드
    private fun fetchToken(code: String) {
        authRepository.fetchToken(
            accountType = "kakao",
            code = code,
            onSuccess = { token, isWelcomeMember ->
                if (isWelcomeMember) {
                    navigateToOnboarding(token)
                } else {
                    navigateToMain(token)
                }
            },
            onFailure = { throwable ->
                throwable.printStackTrace()
                // 실패 시 사용자에게 알림을 표시하거나 처리
            }
        )
    }

    // OnboardingActivity로 이동
    private fun navigateToOnboarding(token: String) {
        val intent = Intent(this, OnboardingActivity::class.java).apply {
            putExtra("token", token)
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToMain(token: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("token", token)
        }
        startActivity(intent)
        finish()
    }
}

/*
    // Kakao 로그인 버튼 클릭 시 WebView를 전체 화면으로 표시하는 메서드
    private fun showWebView() {
        val kakaoLoginUri = "https://kauth.kakao.com/oauth/authorize?client_id=${BuildConfig.KAKAO_CLIENT_ID}&redirect_uri=${BuildConfig.KAKAO_REDIRECT_URI}&response_type=code"

        // 다른 UI 요소 숨기기
        binding.profileImage.visibility = View.GONE
        binding.kakaoLoginButton.visibility = View.GONE
        binding.localLoginButton.visibility = View.GONE

        // WebView 보이기
        binding.webview.visibility = View.VISIBLE
        binding.webview.loadUrl(kakaoLoginUri)
    }*/