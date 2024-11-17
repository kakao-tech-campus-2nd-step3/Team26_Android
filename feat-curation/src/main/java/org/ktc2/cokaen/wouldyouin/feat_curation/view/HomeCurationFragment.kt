package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
                Log.d("Fragment", "Adapter click listener triggered at position: $position")
                val curationId = viewModel.curationList.value?.get(position)?.id
                if (curationId != null) {
                    Log.d("Fragment", "Starting detail activity with curation id: $curationId")
                    startCurationDetailActivity(curationId)
                } else {
                    Log.e("Fragment", "CurationId is null for position: $position")
                }
            }
        })


        // RecyclerView에 Adapter 설정
        binding.curationCard.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@HomeCurationFragment.adapter
            setHasFixedSize(true)
            isNestedScrollingEnabled = true
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    // 원하는 간격을 dp 단위로 설정
                    val spacing = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8f, // 8dp
                        resources.displayMetrics
                    ).toInt()

                    outRect.top = spacing
                    outRect.bottom = spacing
                }
            })
        }




        viewModel.curationList.observe(viewLifecycleOwner) { curations ->
            if (curations != null) {
                Log.d("EMPTY", "Observer triggered with size: ${curations.size}")
            }

            if (curations != null) {
                if (viewModel.isLoading.value != true && curations.isEmpty()) {
                    Log.d("EMPTY", "Showing empty view")
                    binding.curationCard.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                } else {
                    Log.d("EMPTY", "Showing content")
                    binding.curationCard.visibility = View.VISIBLE
                    binding.emptyView.visibility = View.GONE
                    adapter.setData(curations)
                }
            }
        }

        // isLoading observer 추가
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d("EMPTY", "Loading state changed: $isLoading")
            if (!isLoading) {
                // 로딩이 끝났을 때 현재 리스트가 비어있는지 다시 확인
                val currentList = viewModel.curationList.value
                if (currentList.isNullOrEmpty()) {
                    binding.curationCard.visibility = View.GONE
                    binding.emptyView.visibility = View.VISIBLE
                }
            }
        }

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
        Log.d("EMPTY", "Observer setup completed")
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
