package com.example.feat_likes.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feat_likes.adapter.LikedHostAdapter
import com.example.feat_likes.viewModel.HostLikesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.HostLikesBinding

@AndroidEntryPoint
class HostLikesFragment : Fragment() {
    private val viewModel: HostLikesViewModel by viewModels()
    private lateinit var binding: HostLikesBinding
    private lateinit var navigationUtil: NavigationUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HostLikesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HostLikesFragment.viewModel
        }

        Log.d("curator", "called")
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = LikedHostAdapter()

        binding.likedMembersList.adapter = adapter

        adapter.onItemClick = { member ->
            ToastUtils.showShortToast(requireContext(),"${member.nickname} clicked!")
        }

        // 클릭 이벤트 처리
        adapter.onItemClick = { member ->
            lifecycleScope.launch {
                val success = viewModel.postLike(member.memberId)
                if (success) {
                    ToastUtils.showShortToast(requireContext(), "${member.nickname} 좋아요 취소됨")

                    // UI 업데이트: 리스트에서 항목 제거 후 갱신
                    val updatedList = adapter.currentList.filter { it.memberId != member.memberId }
                    adapter.submitList(updatedList)
                } else {
                    ToastUtils.showShortToast(requireContext(), "좋아요 취소 실패")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.likesList.collect { list ->
                adapter.submitList(list)
            }
        }
    }

    private fun startMemberProfileActivity(hostId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.HOST_PROFILE_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("hostId" to hostId.toString()) // 여기에 필요한 데이터(id) 넘겨주시면 됩니다!!
            )
        )
    }
}