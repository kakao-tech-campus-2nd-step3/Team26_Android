package org.ktc2.cokaen.wouldyouin

import NavigationHandler
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.feat_onboarding.view.OnboardingActivity
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
        return prefs.getBoolean("need_onboarding", true)
    }
}

//    private fun openFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
