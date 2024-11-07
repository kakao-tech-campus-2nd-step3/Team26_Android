package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this

        //이벤트 상세 페이지에(EventDetail)서 프로필 보기를 누른 후 userId 전달 받을 경우
        //val userId = intent.getStringExtra("userId")
    }
}