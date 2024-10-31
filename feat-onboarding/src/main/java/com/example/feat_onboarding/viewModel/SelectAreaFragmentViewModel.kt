package com.example.feat_onboarding.viewModel

import android.util.Log
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
class SelectAreaViewModel @Inject constructor() : ViewModel() {
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
        // 추후 수정
        Log.d("Selected", selectedGender.value.toString())
        Log.d("Selected", selectedRegion.value.toString())
    }
}