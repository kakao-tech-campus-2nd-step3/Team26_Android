package org.ktc2.cokaen.wouldyouin.feat_event.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
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

        binding.imageViewBand.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java).apply {
                putExtra("CATEGORY_TYPE", "Band")
            }
            startActivity(intent)
        }

        binding.imageViewPlayMusical.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java).apply {
                putExtra("CATEGORY_TYPE", "PlayMusical")
            }
            startActivity(intent)
        }

        binding.imageViewOnedayclass.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java).apply {
                putExtra("CATEGORY_TYPE", "OnedayClass")
            }
            startActivity(intent)
        }

        binding.imageViewExhibition.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java).apply {
                putExtra("CATEGORY_TYPE", "Exhibition")
            }
            startActivity(intent)
        }


        return binding.root
    }
}
