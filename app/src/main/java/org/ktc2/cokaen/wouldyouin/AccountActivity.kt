package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityAccountBinding
import org.ktc2.cokaen.wouldyouin.databinding.ActivitySearchBinding

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_account)
        binding.account = this
    }
}