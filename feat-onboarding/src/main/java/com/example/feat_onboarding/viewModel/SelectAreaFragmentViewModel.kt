package com.example.feat_onboarding.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.MemberAdditionalInfoRequest
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import org.ktc2.cokaen.wouldyouin.network.repository.AuthAPIRepository
import javax.inject.Inject

@HiltViewModel
class SelectAreaViewModel @Inject constructor(
    private val authRepository: AuthAPIRepository,
    private val authPrefs: AuthPreferenceManager
) : ViewModel() {

    private val _selectedGender = MutableStateFlow<String?>(null)
    val selectedGender: StateFlow<String?> = _selectedGender.asStateFlow()

    private val _selectedRegion = MutableStateFlow<String?>(null)
    val selectedRegion: StateFlow<String?> = _selectedRegion.asStateFlow()

    val isNextButtonEnabled = combine(selectedGender, selectedRegion) { gender, region ->
        !gender.isNullOrEmpty() && !region.isNullOrEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState = _uiState.asStateFlow()

    data class OnboardingUiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val isNextButtonEnabled: Boolean = false,
        val navigateToMain: Boolean = false
    )

    private var phoneNumber: String? = null
    private var region: String? = null
    private var gender: String? = null

    fun updatePhoneNumber(number: String) {
        phoneNumber = number
        updateNextButtonState()
        // SharedPreferences에 저장
        authPrefs.phone = number
    }

    fun updateRegion(selectedRegion: String) {
        region = selectedRegion
        updateNextButtonState()
        // SharedPreferences에 저장
        authPrefs.area = selectedRegion
    }

    fun updateGender(selectedGender: String) {
        gender = selectedGender
        updateNextButtonState()
        // SharedPreferences에 저장
        authPrefs.gender = selectedGender
    }

    private fun updateNextButtonState() {
        _uiState.update { currentState ->
            currentState.copy(
                isNextButtonEnabled = !phoneNumber.isNullOrEmpty() &&
                        !region.isNullOrEmpty() &&
                        !gender.isNullOrEmpty()
            )
        }
    }

    fun submitUserInfo() {
        if (phoneNumber.isNullOrEmpty() || region.isNullOrEmpty() || gender.isNullOrEmpty()) {
            _uiState.update { it.copy(error = "모든 정보를 입력해주세요") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val request = MemberAdditionalInfoRequest(
                    phone = phoneNumber!!,
                    area = region!!,
                    gender = gender!!
                )

                val response = authRepository.sendAdditionalInfo(request)

                // 토큰 응답 저장
                authPrefs.saveTokenResponse(response)

                // memberType이 welcome이 아닐 때만 메인으로 네비게이션
                if (response.memberType.toString() != "welcome") {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            navigateToMain = true
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = "추가 정보 등록이 필요합니다"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "추가 정보 등록에 실패했습니다"
                    )
                }
            }
        }
    }
}