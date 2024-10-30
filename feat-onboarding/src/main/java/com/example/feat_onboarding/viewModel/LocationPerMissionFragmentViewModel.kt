package com.example.feat_onboarding.viewModel

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LocationPerMissionFragmentViewModel (
    private val repository: LocationPermissionRepository
) : ViewModel() {
    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.Initial)
    val permissionState: StateFlow<PermissionState> = _permissionState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEvent: SharedFlow<NavigationEvent> = _navigationEvent.asSharedFlow()

    fun checkPermissionStatus(activity: Activity) {
        when {
            repository.hasLocationPermission() -> {
                viewModelScope.launch {
                    _navigationEvent.emit(NavigationEvent.NavigateToNext)
                }
            }
            repository.shouldShowPermissionRationale(activity) -> {
                _permissionState.value = PermissionState.ShowRationale
            }
            else -> {
                _permissionState.value = PermissionState.RequestPermission
            }
        }
    }

    fun onPermissionResult(granted: Boolean) {
        viewModelScope.launch {
            if (granted) {
                _navigationEvent.emit(NavigationEvent.NavigateToNext)
            } else {
                _permissionState.value = PermissionState.ShowSettings
            }
        }
    }

    fun onNextButtonClicked() {
        viewModelScope.launch {
            _navigationEvent.emit(NavigationEvent.NavigateToNext)
        }
    }
}

sealed class PermissionState {
    object Initial : PermissionState()
    object RequestPermission : PermissionState()
    object ShowRationale : PermissionState()
    object ShowSettings : PermissionState()
}

sealed class NavigationEvent {
    object NavigateToNext : NavigationEvent()
}