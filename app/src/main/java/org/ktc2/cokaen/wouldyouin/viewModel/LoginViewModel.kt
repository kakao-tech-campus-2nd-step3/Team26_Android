package org.ktc2.cokaen.wouldyouin.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import dagger.hilt.android.internal.Contexts.getApplication
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
import org.ktc2.cokaen.wouldyouin.network.repository.MemberAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authAPIRepository: AuthAPIRepository,
    private val authPrefs: AuthPreferenceManager,
    private val memberAPIRepository: MemberAPIRetrofitRepository,
    application: Application
) : AndroidViewModel(application) {

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
                // 1. 소셜 로그인
                val socialTokenResponse = authAPIRepository.socialLoginRedirect(
                    accountType = "kakao",
                    code = code
                )

                // 2. 토큰 저장
                authPrefs.saveSocialTokenResponse(socialTokenResponse)

                // 3. welcome이 아닌 경우 (기존 유저)
                if (!socialTokenResponse.isWelcomeMember) {
                    // 프로필 정보 가져오기
                    val profileResponse = memberAPIRepository.getMemberProfile(
                        memberId = socialTokenResponse.memberId,
                        context = getApplication()
                    )

                    // 필수 정보(전화번호, 지역, 성별)가 있는지 확인
                    val hasRequiredInfo = profileResponse?.data?.let { profile ->
                        if (!profile.phoneNumber.isNullOrEmpty() &&
                            !profile.area.isNullOrEmpty()&&
                            !profile.gender.isNullOrEmpty()
                        ) {
                            // 정보가 있으면 저장하고 true 반환
                            authPrefs.apply {
                                phone = profile.phoneNumber
                                area = profile.area
                                gender = profile.gender
                                nickname = profile.nickname
                                memberId = profile.memberId
                            }
                            true
                        } else {
                            false
                        }
                    } ?: false

                    // 필수 정보 유무에 따라 네비게이션 결정
                    val navigation = if (hasRequiredInfo) {
                        Navigation.ToMain
                    } else {
                        Navigation.ToOnboarding  // 필수 정보가 없으면 온보딩으로
                    }

                    _uiState.update {
                        it.copy(isLoading = false, navigation = navigation)
                    }
                } else {
                    // welcome 유저는 바로 온보딩으로
                    _uiState.update {
                        it.copy(isLoading = false, navigation = Navigation.ToOnboarding)
                    }
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
