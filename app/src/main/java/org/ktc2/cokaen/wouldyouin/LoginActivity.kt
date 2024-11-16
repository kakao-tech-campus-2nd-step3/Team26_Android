package org.ktc2.cokaen.wouldyouin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import org.ktc2.cokaen.wouldyouin.BuildConfig.KAKAO_REDIRECT_URI
import org.ktc2.cokaen.wouldyouin.databinding.ActivityLoginBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.NavigationEvent
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import org.ktc2.cokaen.wouldyouin.network.repository.AuthAPIRepository
import org.ktc2.cokaen.wouldyouin.viewModel.LoginViewModel
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        setupWebView()
        observeUiState()
    }

    private fun setupWebView() {
        binding.kakaoLoginButton.setOnClickListener {
            binding.webview.apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                        if (url.startsWith(KAKAO_REDIRECT_URI)) {
                            val uri = Uri.parse(url)
                            val code = uri.getQueryParameter("code")
                            if (code != null) {
                                binding.webview.visibility = View.GONE
                                viewModel.fetchToken(code)
                            }
                            return true
                        }
                        return false
                    }
                }
                settings.javaScriptEnabled = true
                binding.apply {
                    profileImage.visibility = View.GONE
                    kakaoLoginButton.visibility = View.GONE
                    localLoginButton.visibility = View.GONE
                    webview.visibility = View.VISIBLE
                }
                loadUrl("https://kauth.kakao.com/oauth/authorize?client_id=${BuildConfig.KAKAO_CLIENT_ID}&redirect_uri=${BuildConfig.KAKAO_REDIRECT_URI}&response_type=code")
            }
        }
    }

    private fun observeUiState() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // 에러 처리
                state.error?.let { error ->
                    Toast.makeText(this@LoginActivity, error, Toast.LENGTH_LONG).show()
                }

                // 네비게이션 처리
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