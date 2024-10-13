package org.ktc2.cokaen.wouldyouin

import NavigationHandler
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommandBuilder
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        navigationHandler = NavigationHandler(this, navController)

        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.selectedItemId = R.id.nav_home

        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_search -> navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.search_deeplink_url)
                R.id.nav_book -> navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.booking_deeplink_url)
                R.id.nav_home -> navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.home_curation_deeplink_url)
                R.id.nav_like -> navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.likes_deeplink_url)
                R.id.nav_profile -> navigateToFragment(org.ktc2.cokaen.wouldyouin.core.navigation.R.string.profile_deeplink_url)
                else -> false
            }
            true
        }
    }

    private fun navigateToFragment(deepLinkResId: Int): Boolean {
        val command = NavigationCommandBuilder()
            .to(NavigationDestination.Fragment(deepLinkResId))
            .build()
        navigationHandler.navigate(command)
        return true
    }
}

//    private fun openFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, fragment)
//            .addToBackStack(null)
//            .commit()
//    }
