package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.search = this
        Log.d("test", "search")

        binding.imageViewBand.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewPlayMusical.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewOnedayclass.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewExhibition.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        return binding.root
    }
}
