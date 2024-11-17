package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.data.model.MemberType
import org.ktc2.cokaen.wouldyouin.feat_profile.R
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.CurationAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.HashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.PostAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ActivityCuratorProfileBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.CurationViewModel
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.LikesViewModel
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.ProfileViewModel
import javax.inject.Inject

@AndroidEntryPoint
class CuratorProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCuratorProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val curationViewModel: CurationViewModel by viewModels()
    private val likesViewModel: LikesViewModel by viewModels()

    @Inject
    lateinit var navigationUtil: NavigationUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuratorProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        val curatorId = intent.getStringExtra("curatorId")?.toLongOrNull()
        if (curatorId != null) {
            profileViewModel.fetchMemberProfile(curatorId, this)
            curationViewModel.loadCurations(curatorId)
            setupLikeButton(curatorId)
        } else {
            ToastUtils.showShortToast(this, "큐레이터 정보를 찾을 수 없습니다.")
            finish()
        }

        setupObservers(curatorId)
    }

    private fun setupObservers(curatorId: Long?) {
        // ViewModel의 데이터를 관찰하여 UI 업데이트
        profileViewModel.memberProfile.observe(this) { memberResponse ->
            memberResponse?.data?.let { member ->
                binding.nickname.text = member.nickname
                binding.role.text = member.memberType.toString()
                binding.likes.text = member.likes.toString()
                binding.intro.text = member.intro
                binding.emali.text = member.email

                //프로필 이미지
                binding.imageUrl = member.profileUrl

                setupHashtagRecyclerView(member.hashtag)
            }
        }

        lifecycleScope.launch {
            curationViewModel.curations.collect { curations ->
                curatorId?.let {
                    setupCurationRecyclerView(curations, it)
                }
            }
        }
    }

    private fun setupHashtagRecyclerView(hashtags: List<String>) {
        val hashtagAdapter = HashtagAdapter(hashtags)
        binding.hashtag.apply {
            layoutManager = LinearLayoutManager(
                this@CuratorProfileActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = hashtagAdapter
        }
    }

    private fun setupCurationRecyclerView(curations: List<CurationResponse>, curatorId: Long) {
        val curationAdapter = CurationAdapter(curations)
        binding.posts.apply {
            adapter = curationAdapter
            layoutManager = LinearLayoutManager(
                this@CuratorProfileActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    this@CuratorProfileActivity,
                    DividerItemDecoration.HORIZONTAL
                )
            )
            addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    // 원하는 간격을 dp 단위로 설정
                    val spacing = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        8f, // 8dp
                        resources.displayMetrics
                    ).toInt()

                    outRect.top = spacing
                    outRect.bottom = spacing
                }
            })

            curationAdapter.setOnItemClickListener { curation ->
                startActivityTo(curation.id)
            }

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                    if (!curationViewModel.isLoading.value && totalItemCount <= lastVisibleItem + 5) {
                        curationViewModel.loadCurations(curatorId)
                    }
                }
            })
        }
    }

    private fun startActivityTo(curationId: Long) {
        val command = NavigationCommand(
            destination = NavigationDestination.Activity(DeepLinkDestinations.DETAIL_CURATION_DEEPLINK),
            data = mapOf("curationId" to curationId.toString()),
            activityOptions = ActivityNavigationOptions(
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        navigationUtil.navigate(command)
    }

    private fun setupLikeButton(curatorId: Long) {
        likesViewModel.checkIfLiked(curatorId, MemberType.curator)

        lifecycleScope.launch {
            likesViewModel.isLiked.collect { isLiked ->
                updateLikeButtonColor(isLiked)
            }
        }

        binding.likeButton.setOnClickListener {
            likesViewModel.toggleLike(curatorId)
        }
    }

    private fun updateLikeButtonColor(isLiked: Boolean) {
        val color = if (isLiked) {
            Color.parseColor("#FF0000")
        } else {
            Color.parseColor("#808080")
        }
        binding.likeButton.setColorFilter(color, PorterDuff.Mode.SRC_IN)
    }

}