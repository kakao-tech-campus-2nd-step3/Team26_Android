package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.FragmentSearch1Binding

class SearchFragment1 : Fragment() {

    private lateinit var binding: FragmentSearch1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search1, container, false)

        return binding.root
    }
}
