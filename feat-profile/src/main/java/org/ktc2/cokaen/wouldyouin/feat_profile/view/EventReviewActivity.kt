package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.feat_profile.R
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.PendingReviewAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ActivityEventReviewBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.EventReviewViewModel

@AndroidEntryPoint
class EventReviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventReviewBinding
    private val viewModel: EventReviewViewModel by viewModels()
    private val reviewAdapter = PendingReviewAdapter { eventId ->
        showReviewDialog(eventId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupObservers() {
        binding.btnBack.setOnClickListener {
            finish()
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pendingReviews.collect { reviews ->
                        reviewAdapter.submitList(reviews)
                        binding.emptyView.isVisible = reviews.isEmpty() && !viewModel.loading.value
                        binding.recyclerView.isVisible = reviews.isNotEmpty()
                    }
                }

                launch {
                    viewModel.loading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading && viewModel.pendingReviews.value.isEmpty()
                    }
                }

                launch {
                    viewModel.reviewSubmitResult.collect { success ->
                        if (success) {
                           ToastUtils.showShortToast(this@EventReviewActivity, "등록이 완료되었습니다.")
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@EventReviewActivity)
            addItemDecoration(
                DividerItemDecoration(this@EventReviewActivity, DividerItemDecoration.VERTICAL)
            )

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!viewModel.loading.value && totalItemCount <= lastVisibleItem + 5) {
                        viewModel.loadPendingReviewList()
                    }
                }
            })
        }
    }

    private fun showReviewDialog(eventId: Long) {
        ReviewDialog.newInstance(eventId)
            .show(supportFragmentManager, "review_dialog")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}