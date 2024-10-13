package org.ktc2.cokaen.wouldyouin

import NavigationHandler
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommandBuilder
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMainBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.view.HomeCurationFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lateinit var navigationHandler: NavigationHandler

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navigationHandler = NavigationHandler(this, navController)

//        // 기본 화면은 SearchFragment로 설정
//        openFragment(SearchFragment())

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> {
                    navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.search_deeplink_url, navigationHandler)
                    true
                }
                R.id.nav_book -> {
                    navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.booking_deeplink_url, navigationHandler)
                    true
                }
                R.id.nav_home -> {
                    navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.home_curation_deeplink_url, navigationHandler)
                    true
                }
                R.id.nav_like -> {
                    navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.booking_deeplink_url, navigationHandler)
                    true
                }
                R.id.nav_profile -> {
                    navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.profile_deeplink_url, navigationHandler)
                    true
                }
                else -> false
            }
        }
    }

//    private fun openFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }

    private fun navigateToFragment(deepLinkResId: Int, navigationHandler: NavigationHandler) {
        val command = NavigationCommandBuilder()
            .to(NavigationDestination.Fragment(deepLinkResId))
            .build()
        navigationHandler.navigate(command)
    }
}
