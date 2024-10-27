package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.FragmentAccount1Binding

class AccountFragment1 : Fragment() {

    private lateinit var binding: FragmentAccount1Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account1, container, false)
//        binding.account = this

        return binding.root
    }
}
