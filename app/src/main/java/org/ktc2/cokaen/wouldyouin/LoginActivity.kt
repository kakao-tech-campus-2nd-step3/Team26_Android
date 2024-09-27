package org.ktc2.cokaen.wouldyouin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import org.ktc2.cokaen.wouldyouin.databinding.ActivityLoginBinding
import org.ktc2.cokaen.wouldyouin.databinding.ActivitySearchBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.login = this
    }
}