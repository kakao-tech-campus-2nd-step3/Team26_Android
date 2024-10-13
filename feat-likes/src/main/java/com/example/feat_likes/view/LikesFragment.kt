package com.example.feat_likes.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.feat_likes.viewModel.LikesViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.ktc2.cokaen.wouldyouin.feat_likes.R
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.FragmentLikesBinding

class LikesFragment : Fragment() {

    private var _binding: FragmentLikesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LikesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("test", "likes")

        setupToolbar()
        setupViewPager()
    }

    private fun setupToolbar() {
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.notificationButton.setOnClickListener {
            navigateToNotifications()
        }
    }

    private fun navigateToNotifications() {
        findNavController().navigate(R.id.notificationsFragment)
    }
    private fun setupViewPager() {
        val pagerAdapter = LikesPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "주최자"
                1 -> "큐레이터"
                else -> "Tab ${position + 1}"
            }
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class LikesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2 // 탭의 수

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OrganizerLikesFragment()
                1 -> CuratorLikesFragment()
                else -> throw IllegalArgumentException("Invalid position $position")
            }
        }
    }
}