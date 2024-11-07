package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchResultBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter

class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        binding.searchResult = this


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
