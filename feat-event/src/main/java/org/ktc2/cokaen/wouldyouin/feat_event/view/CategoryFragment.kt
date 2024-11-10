package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentCategoryBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.EventViewModel

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var eventAdapter: EventAdapter
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.category = this

        // RecyclerView 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        eventAdapter = EventAdapter(emptyList())
        binding.recyclerView.adapter = eventAdapter

        // ViewModel에서 이벤트 목록을 관찰하여 RecyclerView 업데이트
        /*
        eventViewModel.eventList.observe(viewLifecycleOwner, Observer { events ->
            events?.let {
                eventAdapter = EventAdapter(it.data.events)
                binding.recyclerView.adapter = eventAdapter
            }
        })*/
        eventViewModel.eventList.observe(viewLifecycleOwner, Observer { eventList ->
            eventList?.data?.events?.let { events ->
                eventAdapter = EventAdapter(events)
                binding.recyclerView.adapter = eventAdapter
            }
        })

        // 샘플 위치 값을 사용하여 이벤트 목록 가져오기
        val startLatitude = 35.1684  // 시작 지점의 위도
        val startLongitude = 126.8996  // 시작 지점의 경도
        val endLatitude = 35.1884  // 끝 지점의 위도
        val endLongitude = 126.9196  // 끝 지점의 경도
        val latitude = 35.1784  // 사용자의 현재 위치 위도
        val longitude = 126.9096  // 사용자의 현재 위치 경도

        // 이벤트 목록 가져오기 (GET 방식)
        eventViewModel.fetchEventList(
            startLatitude = startLatitude,
            startLongitude = startLongitude,
            endLatitude = endLatitude,
            endLongitude = endLongitude,
            latitude = latitude,
            longitude = longitude,
            context = requireContext()
        )

        return binding.root
    }
}


/*
package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentCategoryBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter

class CategoryFragment : Fragment() {

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        binding.category = this

        binding.mapButton.setOnClickListener {
            findNavController().navigate(R.id.action_categoryFragment_to_mapFragment)
        }

        val events = listOf(
            EventRequest(
                startTime = "2024-02-15 19:00",
                endTime = "2024-02-15T21:00",
                location = Location("광주 문화전당로", 35.1234, 126.1234),
                title = "INTERFERENCE WAVE",
                content = "새벽올림 겨울 공연",
                category = "음악",
                price = 2000,
                eventImages = listOf("https://example.com/image.jpg"),
                totalSeats = 35
            ),
            EventRequest(
                startTime = "2024-02-16T20:00",
                endTime = "2024-02-16T22:00",
                location = Location("서울 코엑스", 37.5123, 127.0565),
                title = "Winter Fest",
                content = "겨울 축제",
                category = "축제",
                price = 3000,
                eventImages = listOf("https://example.com/image2.jpg"),
                totalSeats = 50
            )
        )

        eventAdapter = EventAdapter(events)
        
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        return binding.root
    }
}
*/
