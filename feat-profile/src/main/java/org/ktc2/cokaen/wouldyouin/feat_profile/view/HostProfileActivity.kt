package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.EventResponse
import org.ktc2.cokaen.wouldyouin.feat_event.view.viewmodel.EventViewModel
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ActivityProfileBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.HashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.adapter.PostAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.ProfileViewModel

@AndroidEntryPoint
class HostProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        //이벤트 상세 페이지에(EventDetail)서 프로필 보기를 누른 후 userId 전달 받을 경우
        val hostId = intent.getStringExtra("hostId")?.toLongOrNull()
        Log.d("HostProfileActivity", "Received hostId from intent: $hostId")
        if (hostId != null) {
            Log.d("HostProfileActivity", "Calling with hostId: $hostId")
            profileViewModel.fetchMemberProfile(hostId, this)
            eventViewModel.fetchEventsByHost(hostId, context = this)
        } else {
            Log.e("HostProfileActivity", "HostId is null or invalid")
        }

        // ViewModel의 데이터를 관찰하여 UI 업데이트
        profileViewModel.memberProfile.observe(this) { memberResponse ->
            memberResponse?.data?.let { member ->
                Log.d("HostProfileActivity", "Member data fetched: ${member.nickname}")
                binding.nickname.text = member.nickname
                binding.role.text = member.memberType.toString()
                binding.likes.text = member.likes.toString()
                binding.intro.text = member.intro
                binding.phone.text = member.phoneNumber

                // 해시태그 리사이클러뷰
                setupHashtagRecyclerView(member.hashtag)

                //프로필 이미지
                binding.imageUrl = member.profileUrl
                //관객 리뷰(리사이클러뷰)
            }
        }

        //진행한 행사 리사이클러뷰
        eventViewModel.eventsByHost.observe(this) { response ->
            response?.data?.events?.let { events ->
                Log.d("HostProfileActivity", "Events by host fetched: ${events.size}")
                setupPostRecyclerView(events)
            }
        }
    }

    // 해시태그 리사이클러뷰 설정 메소드
    private fun setupHashtagRecyclerView(hashtags: List<String>) {
        val hashtagAdapter = HashtagAdapter(hashtags)
        binding.hashtag.apply {
            layoutManager = LinearLayoutManager(this@HostProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = hashtagAdapter
        }
    }

    //진행한 행사 리사이클러뷰 설정 메소드
    private fun setupPostRecyclerView(events: List<EventResponse>) {
        val postAdapter = PostAdapter(events)
        binding.post.apply {
            layoutManager = LinearLayoutManager(this@HostProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = postAdapter
        }
    }
}