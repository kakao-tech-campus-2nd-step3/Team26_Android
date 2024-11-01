package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CurationCardAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.FragmentHomeCurationBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.HomeCurationViewModel
import javax.inject.Inject

@AndroidEntryPoint
class HomeCurationFragment : Fragment(), CurationCardAdapter.OnItemClickListener {
    @Inject
    lateinit var navigationUtil: NavigationUtil

    val viewModel: HomeCurationViewModel by viewModels()

    private var _binding: FragmentHomeCurationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeCurationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val regionArray = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, regionArray)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.createCurationButton.setOnClickListener {
            startCreateCurationActivity()
        }

        val curationCardAdapter = CurationCardAdapter(emptyList(), this)
        binding.curationCard.adapter = curationCardAdapter

        val curationList = getCurationList()
        curationCardAdapter.setData(curationList)

        viewModel.curationList.observe(viewLifecycleOwner) { curations ->
            val curationCardAdapter = curations?.let { CurationCardAdapter(it, this) }
            binding.curationCard.adapter = curationCardAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startCreateCurationActivity() {
        val intent = Intent(requireContext(), CreateCurationActivity::class.java)
        startActivity(intent)
    }

    private fun startCurationDetailActivity() {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_CURATION_DEEPLINK),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("curationId" to viewModel.selectedCuration.value!!.curationId))
        )
    }

    override fun onItemClick(position: Int) {
        val curation = viewModel.curationList.value!![position]
        viewModel.selectCuration(curation)
        startCurationDetailActivity()
    }


    private fun getCurationList(): List<CurationRespond> {
        // 수정
        return listOf()
    }
}