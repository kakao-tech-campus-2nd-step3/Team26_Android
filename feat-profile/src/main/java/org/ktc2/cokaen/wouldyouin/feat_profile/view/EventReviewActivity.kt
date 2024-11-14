package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.os.Bundle
import android.view.MenuItem
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
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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

        setupToolbar()
        setupRecyclerView()
        setupObservers()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "리뷰 작성"
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = reviewAdapter
            layoutManager = LinearLayoutManager(this@EventReviewActivity)
            addItemDecoration(
                DividerItemDecoration(this@EventReviewActivity, DividerItemDecoration.VERTICAL)
            )
        }
    }

    private fun setupObservers() {
        // StateFlow 수집을 위한 lifecycleScope 사용
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pendingReviews.collect { reviews ->
                        reviewAdapter.submitList(reviews)
                        updateEmptyView(reviews.isEmpty())
                    }
                }

                launch {
                    viewModel.loading.collect { isLoading ->
                        binding.progressBar.isVisible = isLoading
                    }
                }

                launch {
                    viewModel.error.collect { errorMessage ->
                        errorMessage?.let {
                            showErrorSnackbar(it)
                        }
                    }
                }
            }
        }
    }

    private fun updateEmptyView(isEmpty: Boolean) {
        binding.apply {
            emptyView.isVisible = isEmpty
            recyclerView.isVisible = !isEmpty
        }
    }

    private fun showReviewDialog(eventId: Long) {
        ReviewDialog.newInstance(eventId)
            .show(supportFragmentManager, "review_dialog")
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).apply {
            setAction("확인") { dismiss() }
        }.show()
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