package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CurationHashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.DetailCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCurationDetailBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CurationDetailViewModel
import java.nio.file.Paths.get
import javax.inject.Inject

@AndroidEntryPoint
class CurationDetailActivity @Inject constructor() : AppCompatActivity() {

    private lateinit var binding: ActivityCurationDetailBinding
    private val viewModel: CurationDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_curation_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        var curationId: String? = null

        intent.data?.let { uri ->
            curationId = uri.getQueryParameter("curationId")
        }

        val curationBlockAdapter = DetailCurationBlockAdapter()
        binding.rvCurationBlocks.adapter = curationBlockAdapter

        val hashtagAdapter = CurationHashtagAdapter()
        binding.rvHashtags.adapter = hashtagAdapter

        viewModel.curation.observe(this) { curation ->
            binding.invalidateAll()
        }

        viewModel.curationBlocks.observe(this) { blocks ->
            curationBlockAdapter.submitList(blocks)
        }

        viewModel.hashtags.observe(this) { hashtagsString ->
                hashtagAdapter.submitList(hashtagsString)
        }


        if (curationId != null) {
            viewModel.loadCurationDetail(curationId!!)
        }
    }
}