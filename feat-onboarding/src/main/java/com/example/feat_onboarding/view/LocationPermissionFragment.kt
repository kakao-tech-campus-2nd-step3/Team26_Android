package com.example.feat_onboarding.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.feat_onboarding.R
import com.example.feat_onboarding.databinding.FragmentLocationPermissionBinding
import com.example.feat_onboarding.viewModel.LocationPerMissionFragmentViewModel
import com.example.feat_onboarding.viewModel.NavigationEvent
import com.example.feat_onboarding.viewModel.PermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.pow

@AndroidEntryPoint
class LocationPermissionFragment : Fragment() {
    private var _binding: FragmentLocationPermissionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LocationPerMissionFragmentViewModel by viewModels()

    // 위치 클라이언트 추가
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // 정확한 위치 권한 승인
                viewModel.onPermissionGranted()
                fetchCurrentLocation() // 권한 승인 후 위치 가져오기 호출
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // 대략적인 위치 권한 승인
                viewModel.onPermissionGranted()
                fetchCurrentLocation() // 권한 승인 후 위치 가져오기 호출
            }
            else -> {
                // 권한 거부
                showPermissionDeniedDialog()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBindings()
        setupObservers()
    }

    // 위치 가져오기 함수 추가
    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d("LocationPermissionFragment", "Latitude: $latitude, Longitude: $longitude")

                    // 화면 시작과 끝 좌표 계산
                    val topLeftAndBottomRight = calculateVisibleRegion(latitude, longitude, 15)

                    // 계산된 좌표를 SharedPreferences에 저장
                    saveLocationData(latitude, longitude, topLeftAndBottomRight.first.first, topLeftAndBottomRight.first.second, topLeftAndBottomRight.second.first, topLeftAndBottomRight.second.second)
                } else {
                    Toast.makeText(context, "현재 위치를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "위치 정보를 가져오는 데 실패했습니다.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun setupBindings() {
        binding.permissionButton.setOnClickListener {
            viewModel.checkPermissionStatus(requireActivity())
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.permissionState.collect { state ->
                handlePermissionState(state)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.navigationEvent.collect { event ->
                handleNavigationEvent(event)
            }
        }
    }

    private fun handlePermissionState(state: PermissionState) {
        when (state) {
            PermissionState.RequestPermission -> {
                requestPermission()
            }
            PermissionState.ShowRationale -> {
                showPermissionRationaleDialog()
            }
            PermissionState.ShowSettings -> {
                showPermissionDeniedDialog()
            }
            else -> {}
        }
    }

    private fun handleNavigationEvent(event: NavigationEvent) {
        when (event) {
            NavigationEvent.NavigateToNext -> {
                findNavController().navigate(R.id.action_locationPermission_to_selectArea)
            }
            else -> {}
        }
    }

    private fun requestPermission() {
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    private fun showPermissionRationaleDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("위치 권한 필요")
            .setMessage("주변 행사를 확인하기 위해 위치 권한이 필요합니다.")
            .setPositiveButton("권한 허용") { _, _ ->
                requestPermission()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showPermissionDeniedDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("권한 거부됨")
            .setMessage("위치 권한이 거부되어 있어 주변 행사 추천 기능이 제한됩니다.\n설정에서 권한을 허용하시겠습니까?")
            .setPositiveButton("설정으로 이동") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("권한 없이 계속하기") { _, _ ->
                viewModel.onPermissionDenied()
            }
            .show()
    }

    private fun openAppSettings() {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
            startActivity(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = com.example.feat_onboarding.databinding.FragmentLocationPermissionBinding.inflate(inflater, container, false)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    // calculateVisibleRegion 함수 수정
    private fun calculateVisibleRegion(centerLat: Double, centerLng: Double, zoomLevel: Int): Pair<Pair<Double, Double>, Pair<Double, Double>> {
        val scale = 2.0.pow(zoomLevel.toDouble())
        val halfMapWidthInDegrees = (180 / scale)

        val topLeftLat = centerLat + halfMapWidthInDegrees
        val topLeftLng = centerLng - halfMapWidthInDegrees
        val bottomRightLat = centerLat - halfMapWidthInDegrees
        val bottomRightLng = centerLng + halfMapWidthInDegrees

        return Pair(Pair(topLeftLat, topLeftLng), Pair(bottomRightLat, bottomRightLng))
    }

    private fun saveLocationData(centerLat: Double, centerLng: Double, topLeftLat: Double, topLeftLng: Double, bottomRightLat: Double, bottomRightLng: Double) {
        val sharedPreferences = requireActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("centerLat", centerLat.toString())
            putString("centerLng", centerLng.toString())
            putString("topLeftLat", topLeftLat.toString())
            putString("topLeftLng", topLeftLng.toString())
            putString("bottomRightLat", bottomRightLat.toString())
            putString("bottomRightLng", bottomRightLng.toString())
            apply() // 또는 commit()
        }
    }
}