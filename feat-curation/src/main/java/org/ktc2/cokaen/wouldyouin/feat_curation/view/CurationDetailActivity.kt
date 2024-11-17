package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CurationHashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.DetailCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.DetailCurationEventAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCurationDetailBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CurationDetailViewModel
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import javax.inject.Inject

@AndroidEntryPoint
class CurationDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCurationDetailBinding
    private val viewModel: CurationDetailViewModel by viewModels()

    @Inject
    lateinit var authPrefs: AuthPreferenceManager

    @Inject lateinit var navigationUtil: NavigationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_curation_detail)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupUI()
        observeViewModel()
        loadCurationDetail()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.curation_toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {

                true
            }
            R.id.action_edit -> {


                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupUI() {
        binding.rvCurationBlocks.adapter = DetailCurationBlockAdapter()
        binding.rvHashtags.adapter = CurationHashtagAdapter()
        binding.rvEvents.layoutManager = LinearLayoutManager(this)

        binding.rvEvents.adapter = DetailCurationEventAdapter { event ->
            startEventDetailsActivity(event.eventId)
        }

        binding.rvHashtags.layoutManager = LinearLayoutManager(this).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }

        binding.curatorProfile.setOnClickListener {
            val curatorId = viewModel.curation.value?.curator?.curatorId
            if (curatorId != null) {
                startCuratorActivity(curatorId)
            } else {
                ToastUtils.showShortToast(this, "큐레이터를 찾지 못했습니다")
            }
        }

        viewModel.curation.observe(this) { curation ->
            viewModel.curation.value?.curator?.curatorId?.let { checkEditable(it) }
            if (curation != null) {
                binding.tvIntro.text = curation.curator.intro
            }
        }
    }

    private fun observeViewModel() {
        viewModel.curation.observe(this) { curation ->
            binding.invalidateAll()
        }

        viewModel.curationBlocks.observe(this) { blocks ->
            blocks.forEach { block ->
                Log.d("CurationDetail", "Block title: ${block.subtitle}, body: ${block.content}")
            }
            (binding.rvCurationBlocks.adapter as DetailCurationBlockAdapter).submitList(blocks)
        }

        viewModel.hashtags.observe(this) { hashtagsString ->
            (binding.rvHashtags.adapter as CurationHashtagAdapter).submitList(hashtagsString)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

        viewModel.curationEvents.observe(this) { curationEvents ->
            (binding.rvEvents.adapter as DetailCurationEventAdapter).submitList(curationEvents)
            binding.eventsBox.isVisible = !curationEvents.isNullOrEmpty()
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

    private fun startEventDetailsActivity(eventId: Long) {
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_EVENT_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK,
                    clearTop = true
                ),
                data = mapOf("eventId" to eventId.toString())
            )
        )
    }

    private fun startCuratorActivity(curatorId: Long) {
        val command = NavigationCommand(
            destination = NavigationDestination.Activity(DeepLinkDestinations.CURATOR_PROFILE_ACTIVITY),
            data = mapOf("curatorId" to curatorId.toString()),
            activityOptions = ActivityNavigationOptions(
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        navigationUtil.navigate(command)

    }

    private fun startCreateCurationActivity(curationId: Long) {
        val command = NavigationCommand(
            destination = NavigationDestination.Activity(DeepLinkDestinations.CREATE_CURATION_DEEPLINK),
            data = mapOf("curationId" to curationId.toString()),
            activityOptions = ActivityNavigationOptions(
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
        )
        navigationUtil.navigate(command)

    }

    private fun checkEditable(curationId: Long) {
        val memberId = authPrefs.memberId
        val isEditable = memberId != null && curationId == memberId

        val toolbar: Toolbar = findViewById(R.id.toolbar)

        if (isEditable) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)

            toolbar.inflateMenu(R.menu.curation_toolbar_menu)
            toolbar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        viewModel.deleteCuration(curationId)
                        true
                    }
                    R.id.action_edit -> {
                        startCreateCurationActivity(curationId)
                        true
                    }
                    else -> false
                }
            }
        } else {
            toolbar.menu.clear()
        }
    }

}