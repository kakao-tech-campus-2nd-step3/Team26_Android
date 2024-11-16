package org.ktc2.cokaen.wouldyouin

import NavigationHandler
import android.content.Context
import android.content.Intent
import android.Manifest
import android.app.Activity
import android.app.ActivityManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.feat_onboarding.view.OnboardingActivity
import com.example.feat_onboarding.viewModel.LocationUpdateService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommandBuilder
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMainBinding
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationUtil: NavigationUtil
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authPrefs: AuthPreferenceManager

    private fun checkAuth(): Boolean {
        return !authPrefs.token.isNullOrEmpty()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 토큰 여부
        if (!authPrefs.isAuthenticated()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // welcome 여부
        if (authPrefs.isNeedOnboarding()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        checkLocationPermission(this)

        // 위치 권한이 있는 경우 LocationUpdateService 실행
        if (hasLocationPermission() && !isServiceRunning(LocationUpdateService::class.java)) {
            startLocationService()
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        (navigationUtil as NavigationHandler).setNavController(navController)

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.selectedItemId = R.id.nav_home

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> navigateToFragment(DeepLinkDestinations.SEARCH_DEEPLINK)
                R.id.nav_book -> navigateToFragment(DeepLinkDestinations.BOOKING_DEEPLINK)
                R.id.nav_home -> navigateToFragment(DeepLinkDestinations.HOME_CURATION_DEEPLINK)
                R.id.nav_like -> navigateToFragment(DeepLinkDestinations.LIKES_DEEPLINK)
                R.id.nav_profile -> navigateToFragment(DeepLinkDestinations.PROFILE_DEEPLINK)
                else -> false
            }
            true
        }
    }

    private fun navigateToFragment(deepLinkResId: Int): Boolean {
        val command = NavigationCommandBuilder()
            .to(NavigationDestination.Fragment(deepLinkResId))
            .build()
        navigationUtil.navigate(command)
        return true
    }

    private fun checkNeedOnboarding(): Boolean {
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val needOnboarding = prefs.getBoolean("need_onboarding", true)

        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasLocationPermission = hasFineLocation || hasCoarseLocation

        Log.d("Permission", "정확한 위치 권한: ${if (hasFineLocation) "허용" else "거부"}")
        Log.d("Permission", "대략적 위치 권한: ${if (hasCoarseLocation) "허용" else "거부"}")
        Log.d("Permission", "위치 권한 전체 상태: ${if (hasLocationPermission) "허용됨" else "거부됨"}")
        Log.d("Permission", "need_onboarding 상태: $needOnboarding")

        return needOnboarding
    }

    private fun checkLocationPermission(activity: Activity) {
        val hasLocationPermission = hasLocationPermission()

        if (!hasLocationPermission) {
            showLocationPermissionDialog(activity)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("Permission", "정확한 위치 권한: ${if (hasFineLocation) "허용" else "거부"}")
        Log.d("Permission", "대략적 위치 권한: ${if (hasCoarseLocation) "허용" else "거부"}")

        return hasFineLocation || hasCoarseLocation
    }

    private fun startLocationService() {
        val intent = Intent(this, LocationUpdateService::class.java)
        startService(intent)
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun showLocationPermissionDialog(activity: Activity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("위치 권한 필요")
            .setMessage("주변 행사를 확인하기 위해서는 위치 권한이 필요합니다.\n설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { dialog, _ ->
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                    activity.startActivity(this)
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                Log.d("Permission", "사용자가 권한 설정을 취소함")
            }
            .setCancelable(false)
            .show()
    }
}

//원본
/*
package org.ktc2.cokaen.wouldyouin

import NavigationHandler
import android.content.Context
import android.content.Intent
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.feat_onboarding.view.OnboardingActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommandBuilder
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var navigationUtil: NavigationUtil
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkNeedOnboarding()) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
        } else {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            supportActionBar?.hide()

            checkLocationPermission(this)

            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController
            (navigationUtil as NavigationHandler).setNavController(navController)

            binding.bottomNavigationView.setupWithNavController(navController)

            binding.bottomNavigationView.selectedItemId = R.id.nav_home

            binding.bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.nav_search -> navigateToFragment(DeepLinkDestinations.SEARCH_DEEPLINK)
                    R.id.nav_book -> navigateToFragment(DeepLinkDestinations.BOOKING_DEEPLINK)
                    R.id.nav_home -> navigateToFragment(DeepLinkDestinations.HOME_CURATION_DEEPLINK)
                    R.id.nav_like -> navigateToFragment(DeepLinkDestinations.LIKES_DEEPLINK)
                    R.id.nav_profile -> navigateToFragment(DeepLinkDestinations.PROFILE_DEEPLINK)
                    else -> false
                }
                true
            }
        }
    }

    private fun navigateToFragment(deepLinkResId: Int): Boolean {
        val command = NavigationCommandBuilder()
            .to(NavigationDestination.Fragment(deepLinkResId))
            .build()
        navigationUtil.navigate(command)
        return true
    }

    private fun checkNeedOnboarding(): Boolean {

        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val needOnboarding = prefs.getBoolean("need_onboarding", true)

        val hasFineLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasLocationPermission = hasFineLocation || hasCoarseLocation

        Log.d("Permission", "정확한 위치 권한: ${if (hasFineLocation) "허용" else "거부"}")
        Log.d("Permission", "대략적 위치 권한: ${if (hasCoarseLocation) "허용" else "거부"}")
        Log.d("Permission", "위치 권한 전체 상태: ${if (hasLocationPermission) "허용됨" else "거부됨"}")
        Log.d("Permission", "need_onboarding 상태: $needOnboarding")

        return needOnboarding
    }

    private fun checkLocationPermission(activity: Activity) {
        val hasFineLocation = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        Log.d("Permission", "정확한 위치 권한: ${if (hasFineLocation) "허용" else "거부"}")
        Log.d("Permission", "대략적 위치 권한: ${if (hasCoarseLocation) "허용" else "거부"}")

        if (!hasFineLocation && !hasCoarseLocation) {
            showLocationPermissionDialog(activity)
        }
    }


    private fun showLocationPermissionDialog(activity: Activity) {
        MaterialAlertDialogBuilder(activity)
            .setTitle("위치 권한 필요")
            .setMessage("주변 행사를 확인하기 위해서는 위치 권한이 필요합니다.\n설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { dialog, _ ->
                // 앱 설정 화면으로 이동
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                    activity.startActivity(this)
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
                Log.d("Permission", "사용자가 권한 설정을 취소함")
            }
            .setCancelable(false)  // 뒤로가기 버튼으로 dialog를 닫을 수 없게 설정
            .show()
    }
}
*/
