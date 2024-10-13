package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import org.ktc2.cokaen.wouldyouin.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMain2Binding  // 데이터 바인딩 객체

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 데이터 바인딩 객체 초기화
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNav.setupWithNavController(navController)

        // 바텀 내비게이션 아이템 선택 시 액션 정의
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_likes -> {
                    navController.navigate(R.id.navigation_likes)
                    true
                }
                else -> false
            }
        }
    }
}
