package com.example.feat_likes.view

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
import org.ktc2.cokaen.wouldyouin.data.model.LikeResponse
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.CuratorLikesBinding

@AndroidEntryPoint
class CuratorLikesFragment : Fragment() {
    private val viewModel: CuratorLikesViewModel by viewModels()
    private lateinit var binding: CuratorLikesBinding

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
}
