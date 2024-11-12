package org.ktc2.cokaen.wouldyouin.feat_event.view

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
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.Category
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchBinding
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.SearchViewModel

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        binding.search = this
        Log.d("test", "search")

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
            viewModel.searchEvents(
                //수정 필요
                query = query,
                startLatitude = 37.5665,      // 예시값
                startLongitude = 126.9780,
                endLatitude = 37.5765,
                endLongitude = 126.9880,
                latitude = 37.5665,
                longitude = 126.9780,
                context = requireContext()
            )
            findNavController().navigate(R.id.action_searchFragment_to_searchResultFragment)
            //Toast.makeText(requireContext(), "검색어: $query", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
