package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.Location
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchResultBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.adapter.EventAdapter
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private lateinit var eventAdapter: EventAdapter
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)

        // EventAdapter 초기화 및 설정
        eventAdapter = EventAdapter(emptyList())
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
        }

        // ViewModel의 eventList를 관찰하여 UI 업데이트
        searchViewModel.eventList.observe(viewLifecycleOwner) { eventResponse ->
            val eventList = eventResponse?.data?.events ?: emptyList()
            eventAdapter.submitList(eventList)
        }

        return binding.root
    }
}

//혹시 모를 백업을 위한 기존 코드
    /*
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_result, container, false)
        binding.searchResult = this


        //eventAdapter = EventAdapter(events)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = eventAdapter
        }

        return binding.root
    }
}*/