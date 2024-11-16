package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.EventRequest
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = eventAdapter
        }

        // 전달받은 데이터 처리
        val eventListJson = arguments?.getStringArray("eventList") ?: emptyArray()
        val gson = Gson()
        val eventList = eventListJson.map { gson.fromJson(it, EventResponse::class.java) }

        // RecyclerView 업데이트
        eventAdapter.submitList(eventList)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.action_searchResultFragment_to_searchFragment)
                }
            }
        )

    }
}