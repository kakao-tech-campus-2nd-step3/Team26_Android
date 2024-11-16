package org.ktc2.cokaen.wouldyouin

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.feat_onboarding.view.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.BuildConfig.GOOGLE_REDIRECT_URI
import org.ktc2.cokaen.wouldyouin.BuildConfig.KAKAO_REDIRECT_URI
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.AccountType
import org.ktc2.cokaen.wouldyouin.databinding.ActivityLoginBinding
import org.ktc2.cokaen.wouldyouin.viewModel.LoginViewModel

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        intent.extras?.getString("token_expired")?.toBoolean()?.let { isExpired ->
            if (isExpired) {
                ToastUtils.showShortToast(this, "인증이 만료되어 재로그인이 필요합니다")
            }
        }

        setupLoginButtons()
        observeUiState()
        handleDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeepLink(intent)
    }

    private fun setupLoginButtons() {
        binding.kakaoLoginButton.setOnClickListener {
            launchExternalBrowser(
                AccountType.kakao,
                "https://kauth.kakao.com/oauth/authorize?client_id=${BuildConfig.KAKAO_CLIENT_ID}&redirect_uri=${BuildConfig.KAKAO_REDIRECT_URI}&response_type=code"
            )
        }

        binding.googleLoginButton.setOnClickListener {
            launchExternalBrowser(
                AccountType.google,
                "https://accounts.google.com/o/oauth2/auth?client_id=${BuildConfig.GOOGLE_CLIENT_ID}&redirect_uri=${BuildConfig.GOOGLE_REDIRECT_URI}&response_type=code&scope=email profile"
            )
        }
    }

    private fun launchExternalBrowser(accountType: AccountType, oauthUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(oauthUrl))
        startActivity(intent)
    }

    private fun handleDeepLink(intent: Intent) {
        val uri = intent.data
        if (uri != null && uri.scheme == "wouldyouin" && uri.host == "redirect") {
            val path = uri.path
            if (path?.startsWith("/login/social/") == true) {
                val socialPlatform = path.substringAfterLast("/")
                val code = uri.getQueryParameter("code")

                when (socialPlatform) {
                    "kakao" -> handleSocialLogin(code, AccountType.kakao)
                    "google" -> handleSocialLogin(code, AccountType.google)
                    else -> Log.e("LoginActivity", "Unknown social platform: $socialPlatform")
                }
            }
        }
    }

    private fun handleSocialLogin(code: String?, accountType: AccountType) {
        code?.let {
            viewModel.fetchToken(it, accountType)
        } ?: run {
            Toast.makeText(this, "로그인 실패: 인증 코드를 받지 못했습니다.", Toast.LENGTH_LONG).show()
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                state.error?.let { error ->
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_LONG).show()
                }

                state.navigation?.let { navigation ->
                    when (navigation) {
                        is LoginViewModel.Navigation.ToOnboarding -> {
                            startActivity(Intent(this@LoginActivity, OnboardingActivity::class.java))
                            finish()
                        }
                        is LoginViewModel.Navigation.ToMain -> {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }
            }
        }
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