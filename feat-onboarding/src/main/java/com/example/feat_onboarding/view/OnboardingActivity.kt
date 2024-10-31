package com.example.feat_onboarding.view

import NavigationHandler
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.feat_onboarding.R
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import javax.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {
    @Inject lateinit var navigationUtil: NavigationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.onboarding_nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        (navigationUtil as NavigationHandler).setNavController(navController)
    }
}