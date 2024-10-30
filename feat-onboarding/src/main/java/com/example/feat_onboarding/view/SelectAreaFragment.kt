package com.example.feat_onboarding.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.feat_onboarding.R
import com.example.feat_onboarding.databinding.FragmentSelectAreaBinding

class SelectAreaFragment : Fragment() {
    private var _binding: FragmentSelectAreaBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelectAreaBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val regionArray = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, regionArray)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}