package com.example.feat_likes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.feat_likes.adapter.LikedMemberAdapter
import com.example.feat_likes.viewModel.LikesViewModel
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.MemberLikesBinding

class OrganizerLikesFragment : Fragment() {
    private val viewModel: LikesViewModel by viewModels({ requireParentFragment() })
    private lateinit var binding: MemberLikesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MemberLikesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@OrganizerLikesFragment.viewModel
        }

        setupRecyclerView()
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.likedMembersList.adapter = LikedMemberAdapter()
    }
}