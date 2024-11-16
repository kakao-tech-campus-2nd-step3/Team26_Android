package org.ktc2.cokaen.wouldyouin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import org.ktc2.cokaen.wouldyouin.data.model.SocialTokenResponse
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import org.ktc2.cokaen.wouldyouin.network.repository.AuthAPIRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository,
    private val authPrefs: AuthPreferenceManager
) : ViewModel() {

    data class LoginUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val navigation: Navigation? = null
    )

    sealed class Navigation {
        object ToOnboarding : Navigation()
        object ToMain : Navigation()
    }

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun fetchToken(code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, navigation = null) }

            try {
                val socialTokenResponse = authAPIRepository.socialLoginRedirect(
                    accountType = "kakao",
                    code = code
                )

                // 소셜 로그인 응답 저장
                authPrefs.saveSocialTokenResponse(socialTokenResponse)

                // welcome 멤버면 온보딩으로, 아니면 메인으로
                val navigation = if (socialTokenResponse.isWelcomeMember) {
                    Navigation.ToOnboarding
                } else {
                    Navigation.ToMain
                }

                _uiState.update {
                    it.copy(isLoading = false, navigation = navigation)
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "로그인 중 오류가 발생했습니다"
                    )
                }
            }
        }
    }
}
