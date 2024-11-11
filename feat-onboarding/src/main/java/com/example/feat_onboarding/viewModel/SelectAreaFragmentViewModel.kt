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
import javax.inject.Inject

@HiltViewModel
class SelectAreaViewModel @Inject constructor(
    application: Application
) : ViewModel() {
    private val context = application.applicationContext

    private val _selectedGender = MutableStateFlow<String?>(null)
    val selectedGender: StateFlow<String?> = _selectedGender.asStateFlow()

    private val _selectedRegion = MutableStateFlow<String?>(null)
    val selectedRegion: StateFlow<String?> = _selectedRegion.asStateFlow()

    val isNextButtonEnabled = combine(selectedGender, selectedRegion) { gender, region ->
        !gender.isNullOrEmpty() && !region.isNullOrEmpty()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    fun updateGender(gender: String) {
        _selectedGender.value = gender
    }

    fun updateRegion(region: String) {
        _selectedRegion.value = region
    }

    fun submitUserInfo() {
        // 선택된 값 출력 (디버깅용)
        Log.d("Selected", selectedGender.value.toString())
        Log.d("Selected", selectedRegion.value.toString())

        // Context가 null이 아닐 경우에만 SharedPreferences 접근
        val context = context ?: return // context가 null인 경우 반환하여 처리

        // SharedPreferences 객체 얻기 (Context.MODE_PRIVATE 모드를 사용하여 내부 저장소에 저장)
        val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // LiveData에서 값을 가져와 SharedPreferences에 저장
        selectedGender.value?.let { editor.putString("selectedGender", it) }
        selectedRegion.value?.let { editor.putString("selectedRegion", it) }

        // 변경 사항 저장
        editor.apply()

        // 저장 후 확인 로그
        Log.d("SharedPreferences", "Gender: ${selectedGender.value}, Region: ${selectedRegion.value} saved.")
    }



}