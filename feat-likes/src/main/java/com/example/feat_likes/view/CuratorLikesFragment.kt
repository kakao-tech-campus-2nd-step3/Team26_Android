package com.example.feat_likes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.feat_likes.R
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.FragmentCuratorLikesBinding
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.FragmentOrganizerLikesBinding

class CuratorLikesFragment : Fragment() {
    private lateinit var binding: FragmentCuratorLikesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_curator_likes, container, false)

        return binding.root
    }
}