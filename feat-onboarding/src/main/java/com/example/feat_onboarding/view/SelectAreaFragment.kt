package com.example.feat_onboarding.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.feat_onboarding.R
import com.example.feat_onboarding.databinding.FragmentSelectAreaBinding
import com.example.feat_onboarding.viewModel.SelectAreaViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
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

    private val phoneNumberPattern = Regex("^((010-\\d{4}-\\d{4})|(02-\\d{3,4}-\\d{4}))$")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRegionSpinner()
        setupGenderRadioGroup()
        setupNextButton()
        setupPhoneNumberText()
        observeViewModel()
    }

    private fun setupPhoneNumberText() {
        val phoneNumberEditText = binding.phoneNumberEditText
        val phoneNumberInputLayout = binding.phoneNumberInputLayout

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (phoneNumberPattern.matches(input)) {
                    phoneNumberInputLayout.error = null
                    viewModel.updatePhoneNumber(input)
                } else {
                    phoneNumberInputLayout.error = "올바른 형식으로 입력해주세요 (예: 010-1234-5678, 02-000-0000)"
                }
            }
        })
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                // 로딩 상태 처리
                binding.progressBar.isVisible = state.isLoading
                binding.nextButton.isEnabled = state.isNextButtonEnabled && !state.isLoading

                // 에러 처리
                state.error?.let { error ->
                    ToastUtils.showShortToast(requireContext(), error)
                }

                // 네비게이션 처리
                if (state.navigateToMain) {
                    // need_onboarding false로 설정
                    requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
                        .edit()
                        .putBoolean("need_onboarding", false)
                        .apply()

                    // 기존 navigationUtil 사용하여 메인으로 이동
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
        }
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
                R.id.maleRadioButton -> "MAN"
                R.id.femaleRadioButton -> "WOMAN"
                else -> null
            }
            gender?.let { viewModel.updateGender(it) }
        }
    }

    private fun setupNextButton() {
        binding.nextButton.setOnClickListener {
            viewModel.submitUserInfo()
        }
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