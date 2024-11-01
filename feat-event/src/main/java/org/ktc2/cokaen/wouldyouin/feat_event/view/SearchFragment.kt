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
import androidx.navigation.fragment.findNavController
import org.ktc2.cokaen.wouldyouin.feat_event.R
import org.ktc2.cokaen.wouldyouin.feat_event.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding

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
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewPlayMusical.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewOnedayclass.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        binding.imageViewExhibition.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_categoryFragment)
        }

        return binding.root
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {
            //서버에 검색 요청 보내기
            Toast.makeText(requireContext(), "검색어: $query", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }
}
