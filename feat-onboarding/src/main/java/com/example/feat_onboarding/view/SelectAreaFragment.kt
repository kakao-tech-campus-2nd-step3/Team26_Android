package com.example.feat_onboarding.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feat_onboarding.R
import com.example.feat_onboarding.databinding.FragmentSelectAreaBinding
import com.example.feat_onboarding.viewModel.SelectAreaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import javax.inject.Inject

@AndroidEntryPoint
class SelectAreaFragment : Fragment() {
    @Inject
    lateinit var navigationUtil: NavigationUtil

    private var _binding: FragmentSelectAreaBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectAreaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegionSpinner()
        setupGenderRadioGroup()
        setupNextButton()
        observeViewModel()
    }

    private fun setupRegionSpinner() {
        val regionArray = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, regionArray)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateRegion(regionArray[position])
        }
    }

    private fun setupGenderRadioGroup() {
        binding.genderRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val gender = when (checkedId) {
                R.id.maleRadioButton -> "남성"
                R.id.femaleRadioButton -> "여성"
                else -> null
            }
            gender?.let { viewModel.updateGender(it) }
        }
    }

    private fun setupNextButton() {
        binding.nextButton.setOnClickListener {
            viewModel.submitUserInfo()
            finishOnboarding()
        }
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isNextButtonEnabled.collect { isEnabled ->
                binding.nextButton.isEnabled = isEnabled
                binding.nextButton.alpha = if (isEnabled) 1.0f else 0.5f
            }
        }
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun finishOnboarding() {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            .edit()
            .putBoolean("need_onboarding", false)
            .apply()

        startMainActivity()
    }

    private fun startMainActivity() {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.MAIN_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                )
            )
        )
        requireActivity().finish()
    }
}