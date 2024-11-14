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
import com.example.feat_likes.adapter.LikedCuratorAdapter
import com.example.feat_likes.viewModel.CuratorLikesViewModel
import com.example.feat_likes.viewModel.LikesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.CuratorLikesBinding
import javax.inject.Inject

@AndroidEntryPoint
class CuratorLikesFragment : Fragment() {
    private val viewModel: CuratorLikesViewModel by viewModels()
    private lateinit var binding: CuratorLikesBinding

    @Inject
    lateinit var navigationUtil: NavigationUtil

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = CuratorLikesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@CuratorLikesFragment.viewModel
        }
        Log.d("curator", "called")
        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        val adapter = LikedCuratorAdapter()

        binding.likedMembersList.adapter = adapter

        adapter.onItemClick = { member ->
            startMemberProfileActivity(member.memberId)
            ToastUtils.showShortToast(requireContext(),"${member.nickname} clicked!")
        }

        // 클릭 이벤트 처리
        adapter.onItemClick = { member ->
            lifecycleScope.launch {
                val success = viewModel.postLike(member.memberId)
                if (success) {
                    ToastUtils.showShortToast(requireContext(), "${member.nickname} 좋아요가 취소되었습니다.")

                    // UI 업데이트: 리스트에서 항목 제거 후 갱신
                    val updatedList = adapter.currentList.filter { it.memberId != member.memberId }
                    adapter.submitList(updatedList)
                } else {
                    ToastUtils.showShortToast(requireContext(), "좋아요 취소 실패! 다시 시도해 주세요.")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.likesList.collect { list ->
                adapter.submitList(list)
            }
        }
    }
    private fun startMemberProfileActivity(curatorId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.CURATOR_PROFILE_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("curatorId" to curatorId.toString())
            )
        )
    }
}
