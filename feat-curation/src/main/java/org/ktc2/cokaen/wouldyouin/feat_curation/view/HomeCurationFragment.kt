package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class HomeCurationFragment : Fragment() {

    @Inject
    lateinit var navigationUtil: NavigationUtil

    private val viewModel: HomeCurationViewModel by viewModels() // viewModel 초기화
    private lateinit var adapter: CurationCardAdapter

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

        val sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
        val savedArea = sharedPreferences.getString("selectedRegion", "전체")
        binding.autoCompleteTextView.setText(savedArea, false)

        adapter = CurationCardAdapter(object : CurationCardAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val curationId = viewModel.curationList.value?.get(position)?.id
                if (curationId != null) {
                    startCurationDetailActivity(curationId)
                }
            }
        })

        // RecyclerView에 Adapter 설정
        binding.curationCard.adapter = adapter

        // ViewModel에서 curationList를 관찰
        viewModel.curationList.observe(viewLifecycleOwner, Observer { curations ->
            // 데이터가 변경되면 adapter에 새로운 데이터를 전달
            if (curations != null) {
                adapter.setData(curations)
            }
        })

        // isLoading을 관찰하여 로딩 상태 처리
        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            // 로딩 상태 처리 (예: 스크롤 시 로딩 아이콘 표시)
            if (isLoading) {
                // 로딩 중일 때 UI 변경!!!
            } else {
                // 로딩 완료 후 UI 변경!!!!!
            }
        })

        // 처음 데이터 로딩
        viewModel.loadCurationList(savedArea ?: "전체")

        // 버튼 클릭 시 큐레이션 작성 화면으로 이동
        binding.createCurationButton.setOnClickListener {
            startCreateCurationActivity()
        }

        // RecyclerView 스크롤 리스너 설정 (무한 스크롤)
        binding.curationCard.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isLoading.value!! && totalItemCount <= lastVisibleItem + 5) {
                    viewModel.loadCurationList()
                }
            }
        })

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val selectedArea = regionArray[position]
            sharedPreferences.edit().putString("selectedRegion", selectedArea).apply()
            viewModel.updateSelectedArea(selectedArea)
            Log.d("DropDown", "Changed")
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

    private fun startCurationDetailActivity(curationId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_CURATION_DEEPLINK),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("curationId" to curationId.toString()) // curationId 전달
            )
        )
    }
}
