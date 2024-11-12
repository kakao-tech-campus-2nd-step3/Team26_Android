package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ActivityProfileBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.view.adapter.HashtagAdapter
import org.ktc2.cokaen.wouldyouin.feat_profile.view.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        //이벤트 상세 페이지에(EventDetail)서 프로필 보기를 누른 후 userId 전달 받을 경우
        val hostId = intent.getStringExtra("hostId")?.toLongOrNull()
        if (hostId != null) {
            profileViewModel.fetchMemberProfile(hostId, this)
        }

        // ViewModel의 데이터를 관찰하여 UI 업데이트
        profileViewModel.memberProfile.observe(this) { member ->
            member?.let {
                binding.nickname.text = it.nickname
                binding.role.text = it.memberType.toString()
                binding.likes.text = it.likes.toString()
                binding.intro.text = it.intro
                binding.phone.text = it.phoneNumber

                // 해시태그 리사이클러뷰
                setupHashtagRecyclerView(it.hashtag)

                //프로필 이미지
                //진행한 행사(리사이클러뷰)
                //관객 리뷰(리사이클러뷰)
            }
        }
    }

    // 해시태그 리사이클러뷰 설정 메소드
    private fun setupHashtagRecyclerView(hashtags: List<String>) {
        val hashtagAdapter = HashtagAdapter(hashtags)
        binding.hashtag.apply {
            layoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = hashtagAdapter
        }
    }
}