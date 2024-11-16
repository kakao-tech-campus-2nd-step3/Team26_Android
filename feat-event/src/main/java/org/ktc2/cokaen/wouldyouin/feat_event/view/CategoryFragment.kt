package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.Category
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentCategoryBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.EventViewModel

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var eventAdapter: EventAdapter
    private val eventViewModel: EventViewModel by viewModels()
    private var eventList = listOf<EventResponse>() // MapFragment로 전달할 데이터 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.category = this

        // 전달받은 카테고리 값 수신
        val category = arguments?.getString("category") ?: Category.전체.name
        Log.d("CategoryFragment", "Received category: $category")
        binding.array.text = category // UI에서 카테고리명 표시

        // RecyclerView 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(emptyList())
        binding.recyclerView.adapter = eventAdapter

        // ViewModel에서 이벤트 목록을 관찰하여 RecyclerView 업데이트
        Log.d("FetchEventList", "Fetching events for category: $category")
        eventViewModel.eventListDetail.observe(viewLifecycleOwner, Observer { eventListResponse ->
            eventListResponse?.data?.events?.let { events ->
                eventList = events
                eventAdapter.submitList(events)
            }
        })

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

        // 이벤트 목록 가져오기 (GET 방식)
        eventViewModel.fetchEventListDetail(
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            endLatitude = endLatitude,
            endLongitude = endLongitude,
            latitude = latitude,
            longitude = longitude,
            category = category,
            context = requireContext()
        )

        binding.mapButton.setOnClickListener {
            //findNavController().navigate(R.id.action_categoryFragment_to_mapFragment)

            val gson = Gson()
            val jsonEventList = eventList.map { gson.toJson(it) }.toTypedArray() // JSON 문자열 배열로 변환

            val bundle = Bundle().apply {
                putStringArray("eventList", jsonEventList)
            }
            findNavController().navigate(R.id.action_categoryFragment_to_mapFragment, bundle)
        }

        return binding.root
    }
}
