package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.EventSearchAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.FragmentCurationSearchEventResultBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CurationSearchViewModel

@AndroidEntryPoint
class CurationSearchResultFragment : DialogFragment() {

    private lateinit var binding: FragmentCurationSearchEventResultBinding
    private val viewModel: CurationSearchViewModel by viewModels()
    private lateinit var eventAdapter: EventSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.FullScreenDialog)
        Log.d("SearchQuery", "onCreate Called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("SearchQuery", "onCreateView Called")
        binding = FragmentCurationSearchEventResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        observeEvents()

        arguments?.getString("search_query")?.let { query ->
            Log.d("SearchQuery", "Performing search with query: $query")
            performSearch(query)
            binding.searchQuery.text = query.toString()
        } ?: run {
            ToastUtils.showShortToast(requireContext(), "잘못된 접근입니다.")
            dismiss()
        }

        viewModel.isEmpty.observe(viewLifecycleOwner) { isEmpty ->
            binding.emptyView.visibility = if (isEmpty) View.VISIBLE else View.GONE
            Log.d("SearchQuery", "$isEmpty")
        }

        binding.closeButton.setOnClickListener {
            dismiss()
        }
    }

    private fun onEventSelected(event: EventResponse) {
        val result = Bundle().apply {
            putLong("event_id", event.id)           // 이벤트 ID
            putString("event_name", event.title)    // 이벤트 제목
            putString("host_name", event.host.nickname)  // 호스트 이름
            putString("image_url", event.images[0])  // 이미지 URL
        }

        requireActivity().supportFragmentManager.setFragmentResult("event_selection", result)
        dismiss()
    }

    private fun setupRecyclerView() {
        eventAdapter = EventSearchAdapter { event ->
            onEventSelected(event)
        }
        binding.recyclerView.apply {
            adapter = eventAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeEvents() {
        viewModel.eventList.observe(viewLifecycleOwner) { eventList ->
            eventAdapter.submitList(eventList)
        }
    }

    private fun performSearch(query: String) {
        viewModel.searchEvents(
            query = query,
            startLatitude = 37.5665,
            startLongitude = 126.9780,
            endLatitude = 37.5765,
            endLongitude = 126.9880,
            latitude = 37.5665,
            longitude = 126.9780,
            context = requireContext()
        )
    }
}