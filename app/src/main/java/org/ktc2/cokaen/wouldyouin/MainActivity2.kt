package org.ktc2.cokaen.wouldyouin

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationHandler
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity(), NavigationUtil {

    private lateinit var binding : ActivityMain2Binding
    private lateinit var navigationHandler: NavigationHandler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        navigationHandler = NavigationHandler(navController)

        // 바텀 내비게이션 설정
    }

    override fun navigateToSearchNavigation() = navigationHandler.navigateToSearchNavigation()
    override fun navigateToHomeNavigation() = navigationHandler.navigateToHomeNavigation()
    override fun navigateToProfileNavigation() = navigationHandler.navigateToProfileNavigation()
    override fun navigateToLikesNavigation() = navigationHandler.navigateToLikesNavigation()
    override fun navigateToBookingNavigation() = navigationHandler.navigateToBookingNavigation()
}