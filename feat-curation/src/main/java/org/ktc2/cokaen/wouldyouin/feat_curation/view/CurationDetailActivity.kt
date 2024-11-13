package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CurationHashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.DetailCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCurationDetailBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CurationDetailViewModel

@AndroidEntryPoint
class CurationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurationDetailBinding
    private val viewModel: CurationDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_curation_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupUI()
        observeViewModel()
        loadCurationDetail()
    }

    private fun setupUI() {
        binding.rvCurationBlocks.adapter = DetailCurationBlockAdapter()
        binding.rvHashtags.adapter = CurationHashtagAdapter()
    }

    private fun observeViewModel() {
        viewModel.curation.observe(this) { curation ->
            binding.invalidateAll()
        }

        viewModel.curationBlocks.observe(this) { blocks ->
            (binding.rvCurationBlocks.adapter as DetailCurationBlockAdapter).submitList(blocks)
        }

        viewModel.hashtags.observe(this) { hashtagsString ->
            (binding.rvHashtags.adapter as CurationHashtagAdapter).submitList(hashtagsString)
        }
    }

    private fun loadCurationDetail() {
        val curationId = intent.data?.getQueryParameter("curationId")
        curationId?.let { id ->
            viewModel.loadCurationDetail(id.toLong())
        } ?: run {
            Toast.makeText(this, "큐레이션을 찾을 수 없습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}