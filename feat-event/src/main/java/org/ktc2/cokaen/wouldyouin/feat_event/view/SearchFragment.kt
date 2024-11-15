package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.Category
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.AdAdapter
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.AdViewModel
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()
    private lateinit var adAdapter: AdAdapter
    private val adViewModel: AdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.search = this
        Log.d("test", "search")

        // ViewModel의 eventList 관찰
        searchViewModel.eventList.observe(viewLifecycleOwner) { eventResponse ->
            val eventList = eventResponse?.data?.events ?: emptyList()
            if (eventList.isNotEmpty()) {
                findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
            } else {
                Toast.makeText(requireContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        val searchInput = binding.inputSearchMap
        searchInput.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val query = searchInput.text.toString().trim()
                performSearch(query)
                true
            } else {
                false
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    searchInput.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, resources.getDrawable(R.drawable.drawable_resize, null), null
                    )
                } else {
                    searchInput.setCompoundDrawablesWithIntrinsicBounds(
                        null, null, resources.getDrawable(R.drawable.drawable_resize_x, null), null
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        searchInput.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = searchInput.compoundDrawables[2]
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val touchAreaStart = searchInput.width - searchInput.paddingEnd - drawableWidth
                    if (event.x >= touchAreaStart) {
                        searchInput.text.clear()
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }


        binding.imageViewBand.setOnClickListener {
            //findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
            navigateToCategory(Category.밴드.name)
        }

        binding.imageViewPlayMusical.setOnClickListener {
            //findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
            navigateToCategory(Category.연극.name)
        }

        binding.imageViewOnedayclass.setOnClickListener {
            //findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
            navigateToCategory(Category.원데이클래스.name)
        }

        binding.imageViewExhibition.setOnClickListener {
            //findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
            navigateToCategory(Category.전시회.name)
        }

        // AdAdapter 초기화 및 설정
        adAdapter = AdAdapter(emptyList())
        binding.adRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = adAdapter
        }

        // ViewModel의 adList를 관찰하여 UI 업데이트
        adViewModel.adList.observe(viewLifecycleOwner) { adResponse ->
            val adList = adResponse?.data ?: emptyList()
            if (adList.isNotEmpty()) {
                adAdapter.updateAdList(adList)
            } else {
                Toast.makeText(requireContext(), "광고를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 광고 데이터 가져오기
        adViewModel.fetchAdList(requireContext())

        return binding.root
    }

    private fun navigateToCategory(category: String) {
        val bundle = Bundle().apply {
            putString("category", category)
        }
        Log.d("CategorySelection", "Navigating to category: $category")
        findNavController().navigate(R.id.action_searchFragment_to_categoryFragment, bundle)
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            // SharedPreferences에서 위치 데이터 가져오기
            val sharedPreferences = requireActivity().getSharedPreferences("LocationData", Context.MODE_PRIVATE)

            val latitude = sharedPreferences.getString("centerLat", "0.0")!!.toDouble()
            val longitude = sharedPreferences.getString("centerLng", "0.0")!!.toDouble()
            val startLatitude = sharedPreferences.getString("topLeftLat", "0.0")!!.toDouble()
            val startLongitude = sharedPreferences.getString("topLeftLng", "0.0")!!.toDouble()
            val endLatitude = sharedPreferences.getString("bottomRightLat", "0.0")!!.toDouble()
            val endLongitude = sharedPreferences.getString("bottomRightLng", "0.0")!!.toDouble()

            Log.d("LocationData", "Center Latitude: $latitude, Center Longitude: $longitude")
            Log.d("LocationData", "Top Left Latitude: $startLatitude, Top Left Longitude: $startLongitude")
            Log.d("LocationData", "Bottom Right Latitude: $endLatitude, Bottom Right Longitude: $endLongitude")

            //viewModel.searchEvents(
            searchViewModel.fetchEventList(
                title = query,
                startLatitude = startLatitude,
                startLongitude = startLongitude,
                endLatitude = endLatitude,
                endLongitude = endLongitude,
                latitude = latitude,
                longitude = longitude,
                context = requireContext()
            )
            //findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
            //Toast.makeText(requireContext(), "검색어: $query", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
