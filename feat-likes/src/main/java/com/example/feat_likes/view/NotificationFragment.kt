package com.example.feat_likes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.feat_likes.R
import org.ktc2.cokaen.wouldyouin.feat_likes.databinding.FragmentNotificatoinBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: FragmentNotificatoinBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notificatoin, container, false)

        return binding.root
    }
}